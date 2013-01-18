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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import fr.liglab.adele.cilia.workbench.common.misc.ImageBuilder;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class PropertiesDialog extends WorkbenchDialog {

	private final static String introText = "Available state variables:";
	private final static Point initialSize = new Point(300, 400);

	private final PlatformChain chain;
	private final ComponentRef compoRef;

	public PropertiesDialog(Shell parentShell, PlatformChain chain, ComponentRef compoRef) {
		super(parentShell, compoRef.getId(), initialSize, true, false);
		this.chain = chain;
		this.compoRef = compoRef;
	}

	public Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		container.setLayout(new GridLayout(1, false));

		// Intro
		Label label = new Label(container, SWT.WRAP);
		label.setText(introText);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		CTabFolder folder = new CTabFolder(container, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		folder.setLayout(new GridLayout(1, false));

		// Tab
		new StateVarTab(folder, "State Variables").getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Map<String, String> map = new HashMap<String, String>();
		map.put("un", "1");
		map.put("deux", "2");

		KeyValueTab tab = new KeyValueTab(folder, "tab", "welcome", map, "key", "value");

		folder.setSelection(0);

		return container;
	}
}
