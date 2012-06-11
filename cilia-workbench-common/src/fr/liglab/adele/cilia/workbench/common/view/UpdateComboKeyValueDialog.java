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
package fr.liglab.adele.cilia.workbench.common.view;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.view.editors.ComboKeyValueEditor;

/**
 * 
 * @author Etienne Gandrille
 */
public class UpdateComboKeyValueDialog extends Dialog {

	/** Margin used by the GridLayout. */
	private final int margin = 10;

	private final String windowTitle;
	private final String message;
	private final String keyLabel;
	private final String valueLabel;

	private ComboKeyValueEditor editor;

	private final IInputValidator keyValidator;
	private final IInputValidator valueValidator;

	private final List<String> keyList;
	private final Map<String, String> model;

	/**
	 * Instantiates a new new component instance window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	public UpdateComboKeyValueDialog(Shell parentShell, List<String> keyList, Map<String, String> initialValues,
			String windowTitle, String message, String keyLabel, String valueLabel, IInputValidator keyValidator,
			IInputValidator valueValidator) {
		super(parentShell);
		this.windowTitle = windowTitle;
		this.message = message;
		this.keyList = keyList;
		this.model = initialValues;
		this.keyLabel = keyLabel;
		this.valueLabel = valueLabel;
		this.keyValidator = keyValidator;
		this.valueValidator = valueValidator;
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public Map<String, String> getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		getShell().setText(windowTitle);

		// Global layout
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// Message area
		final Label messageArea = new Label(container, SWT.WRAP);
		messageArea.setText(message);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Main widget
		editor = new ComboKeyValueEditor(container, keyList, model, keyLabel, valueLabel);
		editor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		editor.setKeyValidator(keyValidator);
		editor.setValueValidator(valueValidator);

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
		getButton(IDialogConstants.OK_ID).setEnabled(true);
		editor.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
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
		return new Point(500, 400);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}
}
