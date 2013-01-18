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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview.dialog;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import fr.liglab.adele.cilia.workbench.common.ui.widget.KeyValueWidget;

/**
 * 
 * @author Etienne Gandrille
 */
public class KeyValueTab {

	private final Composite composite;

	public KeyValueTab(CTabFolder folder, String tabTitle, String introMessage, Map<String, String> input, String keyLabel, String valueLabel) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText(tabTitle);
		composite = createComposite(folder, introMessage, input, keyLabel, valueLabel);
		item.setControl(composite);
	}

	public Composite getComposite() {
		return composite;
	}

	public void setLayoutData(Object layoutData) {
		composite.setLayoutData(layoutData);
	}

	private Composite createComposite(Composite parent, String introMessage, Map<String, String> input, String keyLabel, String valueLabel) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		// Label
		final Label label = new Label(composite, SWT.WRAP);
		label.setText(introMessage);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// JFace Table Viewer
		KeyValueWidget kvw = new KeyValueWidget(composite, input, keyLabel, valueLabel);
		kvw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return composite;
	}
}
