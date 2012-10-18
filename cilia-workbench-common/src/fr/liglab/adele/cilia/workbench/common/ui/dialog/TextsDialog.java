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
import java.util.List;

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

import fr.liglab.adele.cilia.workbench.common.misc.Strings;

/**
 * Helper class to build dialogs with label and text fields pairs.
 * 
 *  @author Etienne Gandrille
 */
public class TextsDialog extends WorkbenchDialog  {

	private final String labels[];
	private Text textFields[];
	private final ModifyListener listener = new UpdateListener();
	private final TextsDialogValidator validator;
	private Label messageArea;
	private String values[];
	
	public TextsDialog(Shell parentShell, String title, Point initialSize, boolean isOkButtonEnable, String labels[], TextsDialogValidator validator) {
		super(parentShell, title, initialSize, isOkButtonEnable);
		
		this.labels = labels;
		this.validator = validator;
	}

	/**
	 * Gets the values from the text fields.
	 * 
	 * @return
	 */
	public String[] getValues() {
		return values;
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

		List<Text> textWidgets = new ArrayList<Text>();
		for (String label : labels) {
			// Label
			final Label labelWidget = new Label(container, SWT.WRAP);
			labelWidget.setText(label);
			labelWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

			// Text
			Text textWidget = new Text(container, SWT.NONE);
			textWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			textWidgets.add(textWidget);
			
			// listeners
			textWidget.addModifyListener(listener);
		}
		textFields = textWidgets.toArray(new Text[0]);
		
		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		return container;
	}
	
	private class UpdateListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			
			// Update values
			List<String> newValues = new ArrayList<String>();
			for (Text textField : textFields)
				newValues.add(textField.getText());
			values = newValues.toArray(new String[0]);
			
			// Validate
			String message = null;
			if (validator != null)
				message = validator.validate(e, values);
			
			getButton(IDialogConstants.OK_ID).setEnabled(message == null);
			messageArea.setText(Strings.nullToEmpty(message));			
		}
	}

	public interface TextsDialogValidator {
		
		/**
		 * Validates data in a dialig.
		 * 
		 * @param event the event
		 * @param values an array, which contains text fields values
		 * @return an error message (empty chain IS an error message) or null, if values are valid.
		 */
		public String validate(ModifyEvent event, String values[]); 
	} 
}
