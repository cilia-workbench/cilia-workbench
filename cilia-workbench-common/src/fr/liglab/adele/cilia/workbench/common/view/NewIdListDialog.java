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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.google.common.base.Strings;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class NewIdListDialog extends Dialog {

	/** The listener. */
	private final WindowModifyListener listener = new WindowModifyListener();
	/** Margin used by the GridLayout. */
	private final int margin = 10;

	/* =============== */
	/* Text and labels */
	/* =============== */

	/** The window title. */
	private final String windowTitle;
	/** Label for the ID field. */
	private final String idLabelText;
	/** Label for the list field */
	private final String listLabelText;
	/** Values for the list */
	private final Map<String, Object> listValues;

	/* ====== */
	/* Fields */
	/* ====== */

	/** The id field. */
	private Text idText;
	/** The type field. */
	private Combo listCombo;
	/** The message area. */
	Label messageArea;

	/* ====== */
	/* Result */
	/* ====== */

	/** The component id. */
	private String id;
	/** The component type. */
	private Object value;

	/**
	 * Instantiates a new new component instance window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	protected NewIdListDialog(Shell parentShell, String windowTitle, String idLabelText, String listLabelText,
			Map<String, Object> listValues) {
		super(parentShell);
		this.windowTitle = windowTitle;
		this.idLabelText = idLabelText;
		this.listLabelText = listLabelText;
		this.listValues = listValues;
	}

	public String getId() {
		return id;
	}

	public Object getValue() {
		return value;
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
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// Id Label
		final Label idLabel = new Label(container, SWT.WRAP);
		idLabel.setText(idLabelText);
		idLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Id Text
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// List Label
		final Label listLabel = new Label(container, SWT.WRAP);
		listLabel.setText(listLabelText);
		listLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// List Combo
		listCombo = new Combo(container, SWT.READ_ONLY);

		List<String> list = new ArrayList<String>(listValues.keySet());
		Collections.sort(list);
		for (String item : list)
			listCombo.add(item);
		listCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// Listeners
		idText.addModifyListener(listener);
		listCombo.addModifyListener(listener);

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
		return new Point(500, 250);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

	protected abstract String checkValidValues(String id, Object object);

	/**
	 * Listener invoked each time the user modifies a field in this window.
	 * 
	 * @see WindowModifyEvent
	 */
	private class WindowModifyListener implements ModifyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.
		 * events.ModifyEvent)
		 */
		@Override
		public void modifyText(ModifyEvent e) {
			// updates result
			id = idText.getText();
			value = listValues.get(listCombo.getText());

			// updates message
			String msg = checkValidValues(id, value);
			getButton(IDialogConstants.OK_ID).setEnabled(msg == null);
			messageArea.setText(Strings.nullToEmpty(msg));
		}
	}
}