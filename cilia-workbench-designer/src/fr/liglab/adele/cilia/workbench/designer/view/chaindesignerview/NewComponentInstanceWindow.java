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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class NewComponentInstanceWindow extends Dialog {

	/** The mediators id, found in the jar repository. */
	protected String[] componentsId = new String[0];

	private final String componentName;

	/** The listener. */
	private final WindowModifyListener listener = new WindowModifyListener();
	/** The chain model element. */
	protected final Chain chain;
	/** Margin used by the GridLayout. */
	private final int margin = 10;

	/* =============== */
	/* Text and labels */
	/* =============== */

	/** The window title. */
	private final String windowTitle;
	/** Label for the ID field. */
	private final String idLabelText = "ID";
	/** Label for the Type field */
	private final String typeLabelText = "Type";

	/* ====== */
	/* Fields */
	/* ====== */

	/** The id field. */
	private Text idText;
	/** The type field. */
	private Combo typeCombo;
	/** The message area. */
	Label messageArea;

	/* ====== */
	/* Result */
	/* ====== */

	/** The component id. */
	private String componentId;
	/** The component type. */
	private String componentType;

	/**
	 * Instantiates a new new component instance window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	protected NewComponentInstanceWindow(String componentName, Shell parentShell, Chain chain) {
		super(parentShell);
		Preconditions.checkNotNull(chain);
		Preconditions.checkNotNull(componentName);
		this.componentName = componentName;
		windowTitle = "New " + componentName + " instance";
		this.chain = chain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		getShell().setText(windowTitle);

		// Global layout
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// IdLabel
		final Label idLabel = new Label(container, SWT.WRAP);
		idLabel.setText(idLabelText);
		idLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Id Text
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Type Label
		final Label typeLabel = new Label(container, SWT.WRAP);
		typeLabel.setText(typeLabelText);
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Type Combo
		typeCombo = new Combo(container, SWT.NONE);
		for (String mediatorId : componentsId)
			typeCombo.add(mediatorId);
		typeCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// Listeners
		idText.addModifyListener(listener);
		typeCombo.addModifyListener(listener);

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
	 */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getButton(IDialogConstants.OK_ID).setEnabled(false);

	}

	/**
	 * Gets the component id.
	 * 
	 * @return the component id
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Gets the component type.
	 * 
	 * @return the component type
	 */
	public String getComponentType() {
		return componentType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse .swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(300, 250);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

	protected abstract String checkValidValues(String id, String type);

	/**
	 * Listener invoked each time the user modifies a field in this window.
	 * 
	 * @see WindowModifyEvent
	 */
	private class WindowModifyListener implements ModifyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		@Override
		public void modifyText(ModifyEvent e) {
			String id = idText.getText();
			String type = typeCombo.getText();
			String msg = checkValidValues(id, type);

			getButton(IDialogConstants.OK_ID).setEnabled(msg == null);

			boolean found = false;
			for (int i = 0; i < componentsId.length && !found; i++)
				if (componentsId[i].equalsIgnoreCase(type))
					found = true;

			if (msg == null && !found)
				messageArea.setText("Warning: " + componentName + " type " + type + " doesn't exists in repository.");
			else
				messageArea.setText(Strings.nullToEmpty(msg));

			componentId = idText.getText();
			componentType = typeCombo.getText();
		}
	}
}
