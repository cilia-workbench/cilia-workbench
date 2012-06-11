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
package fr.liglab.adele.cilia.workbench.common.view.editors;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A key value editor, with the key value which can be provided using a combo.
 * 
 * @author Etienne Gandrille
 */
public class ComboKeyValueEditor extends KeyValueEditor {

	public ComboKeyValueEditor(Composite parent, List<String> keyList, Map<String, String> input, String keyLabel,
			String valueLabel) {
		super(parent, input, keyLabel, valueLabel);

		Combo combo = (Combo) getKeyControl();
		for (String key : keyList)
			combo.add(key);
		// combo.select(0); // less visual confusion is nothing is selected at
		// startup
	}

	@Override
	protected Control createKeyControl() {
		return new Combo(widgetComposite, SWT.READ_ONLY);
	}

	@Override
	protected String getKeyValue() {
		Combo combo = (Combo) getKeyControl();
		return combo.getText();
	}

	@Override
	protected void setKeyValue(String value) {
		Combo combo = (Combo) getKeyControl();
		combo.select(getIndex(value));
	}

	@Override
	protected void resetKeyValue() {
		// Combo combo = (Combo) getControl();
		// combo.select(0);
	}

	private int getIndex(String value) {
		Combo combo = (Combo) getKeyControl();
		for (int i = 0; i < combo.getItemCount(); i++) {
			String name = combo.getItem(i);
			if (name.equals(value))
				return i;
		}

		// not found. A general purpose implementation would return -1 or throw
		// an exception
		return 0;
	}
}
