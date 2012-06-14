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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import fr.liglab.adele.cilia.workbench.common.misc.Strings;

/**
 * Base class for implementing small dialogs composed with
 * <ul>
 * <li>A text field</li>
 * <li>A combo</li>
 * <li>A text area, for displaying validation messages</li>
 * </ul>
 * 
 * {@link #checkValidValues(String, Object)} is called vor values validation.
 * 
 * @author Etienne Gandrille
 */
public abstract class TextListDialog extends WorkbenchDialog {

	private final WindowModifyListener listener = new WindowModifyListener();

	// Combo values
	private final Map<String, Object> listValues;

	// Text and labels
	private final String textLabelText;
	private final String listLabelText;

	// SWT widgets
	private Text idText;
	private Combo listCombo;
	Label messageArea;

	// Result
	private String text;
	private Object value;

	protected TextListDialog(Shell parentShell, String windowTitle, String textLabel, String listLabel,
			Map<String, Object> listValues) {
		super(parentShell, windowTitle, new Point(500, 250), false);
		this.textLabelText = textLabel;
		this.listLabelText = listLabel;
		this.listValues = listValues;
	}

	public String getText() {
		return text;
	}

	public Object getValue() {
		return value;
	}

	protected abstract String checkValidValues(String id, Object object);

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		// Global layout
		container.setLayout(new GridLayout(2, false));

		// Text Label
		final Label textLabel = new Label(container, SWT.WRAP);
		textLabel.setText(textLabelText);
		textLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Text Text field
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

	private class WindowModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			// updates result
			text = idText.getText();
			value = listValues.get(listCombo.getText());

			// updates message
			String msg = checkValidValues(text, value);
			getButton(IDialogConstants.OK_ID).setEnabled(msg == null);
			messageArea.setText(Strings.nullToEmpty(msg));
		}
	}
}
