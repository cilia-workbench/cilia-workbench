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
package fr.liglab.adele.cilia.workbench.common.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class NewNameNamespaceDialog extends WorkbenchDialog {

	private NameNamespaceID value;

	/** The listener. */
	private final WindowModifyListener listener = new WindowModifyListener();

	private final boolean emptyNamespaceAllowed;

	/* =============== */
	/* Text and labels */
	/* =============== */

	/** Label for the ID field. */
	private final String nameLabelText;
	/** Label for the Type field */
	private final String namespaceLabelText;

	/* ====== */
	/* Fields */
	/* ====== */

	/** The name field. */
	private Text nameText;
	/** The namespace field. */
	private Text namespaceText;
	/** The message area. */
	Label messageArea;

	/**
	 * Instantiates a new new component instance window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	protected NewNameNamespaceDialog(Shell parentShell, String windowTitle, String nameLabelText,
			String namespaceLabelText, boolean emptyNamespaceAllowed) {
		super(parentShell, windowTitle, new Point(300, 250), false);
		this.nameLabelText = nameLabelText;
		this.namespaceLabelText = namespaceLabelText;
		this.emptyNamespaceAllowed = emptyNamespaceAllowed;
	}

	public NameNamespaceID getValue() {
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

		// Global layout
		container.setLayout(new GridLayout(2, false));

		// Name label
		final Label nameLabel = new Label(container, SWT.WRAP);
		nameLabel.setText(nameLabelText);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Name Text
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Namespace Label
		final Label namespaceLabel = new Label(container, SWT.WRAP);
		namespaceLabel.setText(namespaceLabelText);
		namespaceLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Namespace Text
		namespaceText = new Text(container, SWT.NONE);
		namespaceText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// Listeners
		nameText.addModifyListener(listener);
		namespaceText.addModifyListener(listener);

		return container;
	}

	protected abstract String checkValidValue(NameNamespaceID value);

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
			value = new NameNamespaceID(nameText.getText(), namespaceText.getText());
			String msg = checkValidValue(value);
			if (msg == null && !emptyNamespaceAllowed && Strings.isNullOrEmpty(value.getNamespace()))
				msg = namespaceLabelText + " is empty";
			getButton(IDialogConstants.OK_ID).setEnabled(msg == null);
			if (msg == null && Strings.isNullOrEmpty(value.getNamespace()))
				msg = "WARNING : " + namespaceLabelText + " is empty";
			messageArea.setText(Strings.nullToEmpty(msg));
		}
	}
}
