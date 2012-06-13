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
package fr.liglab.adele.cilia.workbench.common.ui.editors;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A key value editor, with the key value which can be provided using a text
 * field.
 * 
 * @author Etienne Gandrille
 */
public class StandardKeyValueEditor extends KeyValueEditor {

	public StandardKeyValueEditor(Composite parent, Map<String, String> input, String keyLabel, String valueLabel) {
		super(parent, input, keyLabel, valueLabel);
	}

	@Override
	protected Control createKeyControl() {
		return new Text(widgetComposite, SWT.NONE);
	}

	protected String getKeyValue() {
		Text text = (Text) getKeyControl();
		return text.getText();
	}

	protected void resetKeyValue() {
		Text text = (Text) getKeyControl();
		text.setText("");
	}

	@Override
	protected void setKeyValue(String value) {
		Text text = (Text) getKeyControl();
		text.setText(value);
	}
}
