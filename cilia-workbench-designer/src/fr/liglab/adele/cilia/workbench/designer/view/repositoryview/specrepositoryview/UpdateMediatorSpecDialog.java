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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.specrepositoryview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.ui.TextValidatorListener;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.common.ui.editors.ListEditor;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IGenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IGenericPort.PortNature;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.NameProperty;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.Parameter;

/**
 * 
 * @author Etienne Gandrille
 */
public class UpdateMediatorSpecDialog extends WorkbenchDialog {

	private static final String windowTitle = "Spec editor";

	private final String introMessage = "You can edit or create a new specification.";

	// ID
	private String idValue;
	private final String idMessage = "id";
	private Text idControl;

	// namespace
	private String namespaceValue;
	private final String namespaceMessage = "namespace";
	private Text namespaceControl;

	// IN ports
	private List<String> synchroPortsValue = new ArrayList<String>();
	private final String synchroPortsMessage = "In ports:";

	// OUT ports
	private List<String> dispatchPortsValue = new ArrayList<String>();
	private final String dispatchPortsMessage = "Out ports:";

	// Mediator properties
	private final String propertiesMessage = "Properties list";
	private List<String> mediatorProperties = new ArrayList<String>();

	// Scheduler params
	private final String schedulerTitle = "scheduler";
	private List<String> schedulerParam = new ArrayList<String>();

	// processor params
	private final String processorTitle = "processor";
	private List<String> processorParam = new ArrayList<String>();

	// dispatcher params
	private final String dispatcherTitle = "dispatcher";
	private List<String> dispatcherParam = new ArrayList<String>();

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Parent shell. Mandatory for this modal dialog.
	 * @param list
	 * @param input
	 *            initial list
	 */
	public UpdateMediatorSpecDialog(Shell parent, MediatorSpec mediatorSpec) {
		super(parent, windowTitle, new Point(450, 800), true);

		// Global parameters
		this.idValue = ((NameNamespaceID) mediatorSpec.getId()).getName();
		this.namespaceValue = ((NameNamespaceID) mediatorSpec.getId()).getNamespace();

		// Ports
		for (IGenericPort port : mediatorSpec.getPorts())
			if (port.getNature() == PortNature.IN)
				synchroPortsValue.add(port.getName());
			else
				dispatchPortsValue.add(port.getName());

		// Mediator
		for (NameProperty property : mediatorSpec.getProperties())
			mediatorProperties.add(property.getName());

		// Scheduler
		if (mediatorSpec.getScheduler() != null)
			for (Parameter param : mediatorSpec.getScheduler().getParameters())
				schedulerParam.add(param.getName());

		// Processor
		if (mediatorSpec.getProcessor() != null)
			for (Parameter param : mediatorSpec.getProcessor().getParameters())
				processorParam.add(param.getName());

		// Dispatcher
		if (mediatorSpec.getDispatcher() != null)
			for (Parameter param : mediatorSpec.getDispatcher().getParameters())
				dispatcherParam.add(param.getName());
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		// Global layout
		container.setLayout(new GridLayout(1, false));

		// Main composites
		Composite generalComposite = new Composite(container, SWT.NONE);
		generalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite portsComposite = new Composite(container, SWT.NONE);
		portsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite propertiesComposite = new Composite(container, SWT.NONE);
		propertiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite advancedComposite = new Composite(container, SWT.NONE);
		advancedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// General composite
		// =================

		generalComposite.setLayout(new GridLayout(2, false));

		// intro
		final Label introLabel = new Label(generalComposite, SWT.WRAP);
		introLabel.setText(introMessage);
		introLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		// id
		createLabel(generalComposite, idMessage, false);
		idControl = createText(generalComposite, true);
		idControl.setText(idValue);

		// namespace
		createLabel(generalComposite, namespaceMessage, false);
		namespaceControl = createText(generalComposite, true);
		namespaceControl.setText(namespaceValue);

		// Ports composite
		// ===============

		portsComposite.setLayout(new GridLayout(2, false));

		createLabel(portsComposite, synchroPortsMessage, false);
		createLabel(portsComposite, dispatchPortsMessage, false);

		// In ports
		ListEditor inPortsEditor = new ListEditor(portsComposite, synchroPortsValue);
		inPortsEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Out Ports
		ListEditor outPortsEditor = new ListEditor(portsComposite, dispatchPortsValue);
		outPortsEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Properties
		// ==========

		propertiesComposite.setLayout(new GridLayout(1, false));

		Composite compo = createComposite(propertiesComposite, propertiesMessage, mediatorProperties);
		compo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Advanced composite
		// =================

		advancedComposite.setLayout(new GridLayout(1, false));

		CTabFolder folder = new CTabFolder(advancedComposite, SWT.BORDER);

		createSPDTab(folder, schedulerTitle, schedulerParam);
		createSPDTab(folder, processorTitle, processorParam);
		createSPDTab(folder, dispatcherTitle, dispatcherParam);

		folder.setSelection(0);

		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return container;
	}

	private static Composite createComposite(Composite parent, String message, List<String> elements) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);

		createLabel(container, message, true);

		ListEditor listEditor = new ListEditor(container, elements);
		listEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return container;
	}

	private static void createSPDTab(CTabFolder folder, String title, List<String> elements) {
		String label = "You can add parameters, for adding variabiliy on the " + title;

		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText(title);

		Composite container = createComposite(folder, label, elements);

		item.setControl(container);
	}

	private static Text createText(final Composite container, boolean readOnly) {
		int style = SWT.WRAP;
		if (readOnly)
			style |= SWT.READ_ONLY;
		final Text text = new Text(container, style);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text.addListener(SWT.Verify, TextValidatorListener.getTextValidatorListner());
		return text;
	}

	private static Label createLabel(Composite container, String message, boolean horizontalFill) {
		final Label label = new Label(container, SWT.WRAP);
		label.setText(message);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, horizontalFill, false));
		return label;
	}

	public List<String> getInPorts() {
		return synchroPortsValue;
	}

	public List<String> getOutPorts() {
		return dispatchPortsValue;
	}

	public List<String> getMediatorProperties() {
		return mediatorProperties;
	}

	public List<String> getSchedulerParam() {
		return schedulerParam;
	}

	public List<String> getProcessorParam() {
		return processorParam;
	}

	public List<String> getDispatcherParam() {
		return dispatcherParam;
	}
}
