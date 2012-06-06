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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

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

import com.google.common.base.Preconditions;

import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Component;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IMediator;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewBindingWindow extends Dialog {

	public static final String DST_COLUMN = "IN";
	public static final String SRC_COLUMN = "OUT";

	private final Chain chain;

	private final String windowTitle = "New binding";

	/** Margin used by the GridLayout. */
	private final int margin = 10;

	private Combo srcElemCombo;

	private Combo dstElemCombo;

	private Combo srcPortCombo;

	private Combo dstPortCombo;

	private Combo srcCardinalityCombo;

	private Combo dstCardinalityCombo;

	private Label messageArea;

	private final IntegrityListener iListener = new IntegrityListener();

	private String srcElem;
	private String dstElem;
	private String srcPort;
	private String dstPort;
	private String srcCardinality;
	private String dstCardinality;

	protected NewBindingWindow(Shell parentShell, Chain chain) {
		super(parentShell);
		Preconditions.checkNotNull(chain);
		Preconditions.checkNotNull(parentShell);
		this.chain = chain;
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
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

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
		for (MediatorComponent item : chain.getMediators()) {
			srcElemCombo.add(item.getId());
			dstElemCombo.add(item.getId());
		}
		for (AdapterComponent item : chain.getAdapters()) {
			IAdapter adapter = item.getReferencedObject();
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
		srcCardinalityCombo.select(2); // 1...1
		dstCardinalityCombo.select(2); // 1...1

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		// Listeners
		srcElemCombo.addModifyListener(iListener);
		dstElemCombo.addModifyListener(iListener);
		srcPortCombo.addModifyListener(iListener);
		dstPortCombo.addModifyListener(iListener);
		srcCardinalityCombo.addModifyListener(iListener);
		dstCardinalityCombo.addModifyListener(iListener);
		srcElemCombo.addModifyListener(new ComboUpdate(srcElemCombo, srcPortCombo, SRC_COLUMN));
		dstElemCombo.addModifyListener(new ComboUpdate(dstElemCombo, dstPortCombo, DST_COLUMN));

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
		return Cardinality.getCardinality(srcCardinality);
	}

	public Cardinality getDstCardinality() {
		return Cardinality.getCardinality(dstCardinality);
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
		return new Point(550, 300);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

	protected void updateResult() {
		srcElem = srcElemCombo.getText();
		dstElem = dstElemCombo.getText();
		srcPort = srcPortCombo.getText();
		dstPort = dstPortCombo.getText();
		srcCardinality = srcCardinalityCombo.getText();
		dstCardinality = dstCardinalityCombo.getText();
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

			Component i = chain.getComponent(comboElem.getText());

			// adapter
			if (i instanceof AdapterComponent) {
				comboPort.setEnabled(false);
			}

			// mediator
			if (i instanceof MediatorComponent) {
				comboPort.setEnabled(true);
				IMediator mediator = ((MediatorComponent) i).getReferencedObject();
				if (portType.equals(DST_COLUMN)) {
					for (IGenericPort port : mediator.getInPorts())
						comboPort.add(port.getName());
				} else {
					for (IGenericPort port : mediator.getOutPorts())
						comboPort.add(port.getName());
				}

				comboPort.select(0);
			}

			updateResult();
			return;
		}
	}
}
