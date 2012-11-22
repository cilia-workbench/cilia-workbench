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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.common;

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

import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Cardinality;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.Port;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChain;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.InAdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.OutAdapterImplem;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewBindingDialog extends WorkbenchDialog {

	// The parent chain
	private final XMLChain chain;

	private final boolean withCardinalies;

	private static final String windowTitle = "New binding";

	// Results
	private String srcElem;
	private String dstElem;
	private String srcPort;
	private String dstPort;
	private String srcCardinality;
	private String dstCardinality;

	// SWT widgets
	private Combo srcElemCombo;
	private Combo dstElemCombo;
	private Combo srcPortCombo;
	private Combo dstPortCombo;
	private Combo srcCardinalityCombo;
	private Combo dstCardinalityCombo;
	private Label messageArea;

	// Listeners
	private final IntegrityListener iListener = new IntegrityListener();

	// internal
	private static final String DST_COLUMN_KEY = "IN";
	private static final String SRC_COLUMN_KEY = "OUT";

	public NewBindingDialog(Shell parentShell, XMLChain chain, boolean withCardinalies) {
		super(parentShell, windowTitle, new Point(550, 300), false);
		this.chain = chain;
		this.withCardinalies = withCardinalies;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		// Global layout
		container.setLayout(new GridLayout(3, false));

		// Empty element
		Label label1 = new Label(container, SWT.WRAP);
		label1.setText("");
		label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Source
		Label label2 = new Label(container, SWT.WRAP);
		label2.setText("Source");
		label2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Destination
		Label label3 = new Label(container, SWT.WRAP);
		label3.setText("Destination");
		label3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Element
		Label label4 = new Label(container, SWT.WRAP);
		label4.setText("Element");
		label4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Source Element Combo
		srcElemCombo = new Combo(container, SWT.READ_ONLY);
		srcElemCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Destination Element Combo
		dstElemCombo = new Combo(container, SWT.READ_ONLY);
		dstElemCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// populating
		for (MediatorRef item : chain.getMediators()) {
			srcElemCombo.add(item.getId());
			dstElemCombo.add(item.getId());
		}
		for (AdapterRef item : chain.getAdapters()) {
			Adapter adapter = item.getReferencedComponentDefinition();
			if (adapter == null) {
				srcElemCombo.add(item.getId());
				dstElemCombo.add(item.getId());
			} else if (adapter.getType() == AdapterType.IN) {
				srcElemCombo.add(item.getId());
			} else if (adapter.getType() == AdapterType.OUT) {
				dstElemCombo.add(item.getId());
			} else {
				srcElemCombo.add(item.getId());
				dstElemCombo.add(item.getId());
			}
		}

		// Port
		Label label5 = new Label(container, SWT.WRAP);
		label5.setText("Port");
		label5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Source port Combo
		srcPortCombo = new Combo(container, SWT.READ_ONLY);
		srcPortCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Destination port Combo
		dstPortCombo = new Combo(container, SWT.READ_ONLY);
		dstPortCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Cardinalities
		if (withCardinalies) {
			Label label6 = new Label(container, SWT.WRAP);
			label6.setText("Cardinalities");
			label6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// Source cardinality
			srcCardinalityCombo = new Combo(container, SWT.READ_ONLY);
			srcCardinalityCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// Destination cardinality
			dstCardinalityCombo = new Combo(container, SWT.READ_ONLY);
			dstCardinalityCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// populating
			for (Cardinality c : Cardinality.values()) {
				srcCardinalityCombo.add(c.stringId());
				dstCardinalityCombo.add(c.stringId());
			}
			srcCardinalityCombo.select(2); // 1..1
			dstCardinalityCombo.select(2); // 1..1
		}

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		// Listeners
		srcElemCombo.addModifyListener(iListener);
		dstElemCombo.addModifyListener(iListener);
		srcPortCombo.addModifyListener(iListener);
		dstPortCombo.addModifyListener(iListener);

		if (withCardinalies) {
			srcCardinalityCombo.addModifyListener(iListener);
			dstCardinalityCombo.addModifyListener(iListener);
		}

		srcElemCombo.addModifyListener(new ComboUpdate(srcElemCombo, srcPortCombo, SRC_COLUMN_KEY));
		dstElemCombo.addModifyListener(new ComboUpdate(dstElemCombo, dstPortCombo, DST_COLUMN_KEY));

		return container;
	}

	public String getSrcElem() {
		return srcElem;
	}

	public String getDstElem() {
		return dstElem;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public String getDstPort() {
		return dstPort;
	}

	public Cardinality getSrcCardinality() {
		if (withCardinalies)
			return Cardinality.getCardinality(srcCardinality);
		else
			throw new RuntimeException("This dialog doesn't have cardinalities !");
	}

	public Cardinality getDstCardinality() {
		if (withCardinalies)
			return Cardinality.getCardinality(dstCardinality);
		else
			throw new RuntimeException("This dialog doesn't have cardinalities !");
	}

	protected void updateResult() {
		srcElem = srcElemCombo.getText();
		dstElem = dstElemCombo.getText();
		srcPort = srcPortCombo.getText();
		dstPort = dstPortCombo.getText();
		if (withCardinalies) {
			srcCardinality = srcCardinalityCombo.getText();
			dstCardinality = dstCardinalityCombo.getText();
		}
	}

	private class IntegrityListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			String srcElem = srcElemCombo.getText();
			String srcPort = srcPortCombo.getText();
			String dstElem = dstElemCombo.getText();
			String dstPort = dstPortCombo.getText();

			String errorMsg = chain.isNewBindingAllowed(srcElem, srcPort, dstElem, dstPort);

			if (errorMsg != null)
				messageArea.setText(errorMsg);
			else
				messageArea.setText("");

			getButton(IDialogConstants.OK_ID).setEnabled(errorMsg == null);
			updateResult();
		}
	}

	private class ComboUpdate implements ModifyListener {

		private final Combo comboElem;
		private final Combo comboPort;
		private final String portType;

		public ComboUpdate(Combo comboElem, Combo comboPort, String portType) {
			this.comboElem = comboElem;
			this.comboPort = comboPort;
			this.portType = portType;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			comboPort.removeAll();

			ComponentRef i = chain.getComponent(comboElem.getText());

			comboPort.setEnabled(true);

			// adapter
			if (i instanceof AdapterRef) {
				Adapter adapter = ((AdapterRef) i).getReferencedComponentDefinition();

				if (portType.equals(DST_COLUMN_KEY)) {
					for (Port port : ((OutAdapterImplem) adapter).getInPorts())
						comboPort.add(port.getName());
				} else {
					for (Port port : ((InAdapterImplem) adapter).getOutPorts())
						comboPort.add(port.getName());
				}

				comboPort.select(0);
			}

			// mediator
			if (i instanceof MediatorRef) {
				Mediator mediator = ((MediatorRef) i).getReferencedComponentDefinition();
				if (portType.equals(DST_COLUMN_KEY)) {
					for (Port port : mediator.getInPorts())
						comboPort.add(port.getName());
				} else {
					for (Port port : mediator.getOutPorts())
						comboPort.add(port.getName());
				}

				comboPort.select(0);
			}

			updateResult();
			return;
		}
	}
}
