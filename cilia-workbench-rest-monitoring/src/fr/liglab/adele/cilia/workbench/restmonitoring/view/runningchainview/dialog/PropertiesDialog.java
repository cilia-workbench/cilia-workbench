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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.AdapterInstanceRef;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.MediatorInstanceRef;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.CiliaRestHelper;

/**
 * 
 * @author Etienne Gandrille
 */
public class PropertiesDialog extends WorkbenchDialog {

	private final static String introText = "Available state variables:";
	private final static Point initialSize = new Point(600, 400);

	private final PlatformChain chain;
	private final ComponentRef compoRef;
	private final Map<String, String> properties;
	private final Map<String, String> information;

	public PropertiesDialog(Shell parentShell, PlatformChain chain, ComponentRef compoRef) throws CiliaException {
		super(parentShell, compoRef.getId(), initialSize, true, false);
		this.chain = chain;
		this.compoRef = compoRef;

		PlatformID platformID = chain.getPlatform().getPlatformID();
		String chainName = chain.getName();
		String instanceID = compoRef.getId();

		// Information
		if (compoRef instanceof AdapterInstanceRef) {
			information = CiliaRestHelper.getAdapterInformation(platformID, chainName, instanceID);
		} else if (compoRef instanceof MediatorInstanceRef) {
			information = CiliaRestHelper.getMediatorInformation(platformID, chainName, instanceID);
		} else {
			String message = "Unknown component type (" + compoRef.getClass().toString() + ")";
			throw new CiliaException(message);
		}

		// Properties
		if (compoRef instanceof AdapterInstanceRef) {
			properties = CiliaRestHelper.getAdapterProperties(platformID, chainName, instanceID);
		} else if (compoRef instanceof MediatorInstanceRef) {
			properties = CiliaRestHelper.getMediatorProperties(platformID, chainName, instanceID);
		} else {
			String message = "Unknown component type (" + compoRef.getClass().toString() + ")";
			throw new CiliaException(message);
		}
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
		KeyValueTab infoTab = new KeyValueTab(folder, "Information", "welcome", information, "key", "value");
		infoTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		KeyValueTab propertiesTab = new KeyValueTab(folder, "Properties", "welcome", properties, "key", "value");
		propertiesTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		StateVarTab svTab = new StateVarTab(folder, "State Variables");
		svTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		folder.setSelection(0);

		return container;
	}
}
