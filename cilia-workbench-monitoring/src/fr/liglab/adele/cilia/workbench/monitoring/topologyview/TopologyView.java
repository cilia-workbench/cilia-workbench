/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.monitoring.topologyview;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.ViewPart;

import fr.liglab.adele.cilia.event.CiliaFrameworkEvent;
import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;
import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.monitoring.Activator;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.TreeContentProvider;

/**
 * Controls the Eclipse view used to display Cilia chains.
 * 
 * @author Etienne Gandrille
 */
public class TopologyView extends ViewPart implements CiliaFrameworkEvent {

	/** View Id used in the plugin.xml */
	public final static String viewId = "fr.liglab.adele.cilia.workbench.monitoring.topologyview";

	/** Main viewer. */
	private TreeViewer viewer;

	/** Model used by the viewer. */
	private MonitoredApplication model = null;

	/** Message area used to display last model reload date. */
	private Label messageArea;

	/** The message area prefix. */
	private final String messageAreaPrefix = "Last model update: ";

	/** The defaultmessage area text. */
	private final String defaultmessageAreaText = messageAreaPrefix + "not available";

	public TopologyView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new TreeContentProvider(this));
		viewer.setLabelProvider(new CiliaLabelProvider());
		viewer.setInput(model);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setAutoExpandLevel(2);

		// Label
		messageArea = new Label(parent, SWT.WRAP);
		messageArea.setText(defaultmessageAreaText);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Selection provider
		getSite().setSelectionProvider(viewer);
	}

	/**
	 * Gets the model displayed by the view.
	 * 
	 * @return an ApplicationMonitored object
	 */
	public MonitoredApplication getModel() {
		return model;
	}

	/**
	 * Forwards focus to viewer.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Reloads the model and refresh the viewer. This operation is performed in
	 * a separated thread, according to SWT specifications.
	 */
	public void reloadModel() {
		Display display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				reloadModelInternal();
			}
		});
	}

	/**
	 * Reload the model in the current thread.
	 * <ul>
	 * <li>Reload the model in the viewer</li>
	 * <li>Update Reload Required variable</li>
	 * <li>Register for future notification</li>
	 * <li>Update Current time on the message area</li>
	 * </ul>
	 */
	private void reloadModelInternal() {
		MonitoredApplication app = Activator.getMonitoredApplication();

		if (app == null)
			MessageDialog.openError(getSite().getShell(), "Reload model", "ApplicationMonitored is NULL");
		else {
			// Reload the model in the viewer
			this.model = app;
			viewer.setInput(app);

			// Update Reload Required variable
			updateReloadRequiredVariable(false);

			// Register for future notification
			model.getFrameworkListener().register(this, CiliaFrameworkEvent.ALL_EVENTS);

			// Update Current time on the message area
			updateLastReloadTime();
		}
	}

	/**
	 * Update the last reload time in the message area with the current time.
	 */
	private void updateLastReloadTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(CiliaUtil.DATE_FORMAT);
		String currentTime = sdf.format(cal.getTime());
		messageArea.setText(messageAreaPrefix + currentTime);
	}

	/**
	 * Set the reload required variable to a new value.
	 * 
	 * @param newValue
	 *            the new value.
	 */
	private void updateReloadRequiredVariable(boolean newValue) {
		ToggleSourceProvider.setToggleVariable(VariablesSourceProvider.RELOAD_REQUIRED_VARIABLE, newValue);
	}

	/**
	 * Callback used by Cilia notification.
	 * 
	 * @param chainId
	 *            the chain id
	 * @param mediatorId
	 *            the mediator id
	 * @param evtNumber
	 *            the evt number
	 */
	@Override
	public void event(String chainId, String mediatorId, int evtNumber) {
		if (getAutoReloadValue())
			reloadModel();
		else
			updateReloadRequiredVariable(true);
	}

	/**
	 * Gets the auto reload variable value.
	 * 
	 * @return the auto reload variable value.
	 */
	private boolean getAutoReloadValue() {
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service.getCommand("fr.liglab.adele.cilia.workbench.monitoring.command.toggleAutoreload");
		State state = command.getState(RegistryToggleState.STATE_ID);
		if (state == null)
			throw new RuntimeException("The command does not have a toggle state"); //$NON-NLS-1$
		if (!(state.getValue() instanceof Boolean))
			throw new RuntimeException("The command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$

		return ((Boolean) state.getValue()).booleanValue();
	}
}
