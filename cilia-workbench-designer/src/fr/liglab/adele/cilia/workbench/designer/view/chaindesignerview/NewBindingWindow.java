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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Component;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.InPort;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.JarPort;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.OutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewBindingWindow extends Dialog {

	public static final String IN_PORT = "IN";
	public static final String OUT_PORT = "OUT";

	private final Chain chain;

	private final String windowTitle = "New binding";

	/** Margin used by the GridLayout. */
	private final int margin = 10;

	private Combo srcElemCombo;

	private Combo dstElemCombo;

	private Combo srcPortCombo;

	private Combo dstPortCombo;

	private Label messageArea;

	private final IntegrityListener iListener = new IntegrityListener();

	private String srcElem;
	private String dstElem;
	private String srcPort;
	private String dstPort;

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
			NameNamespaceID id = item.getReferencedTypeID();
			GenericAdapter a = JarRepoService.getInstance().getAdapter(id);
			if (a == null) {
				srcElemCombo.add(item.getId());
				dstElemCombo.add(item.getId());
			} else if (a.isInAdapter())
				dstElemCombo.add(item.getId());
			else if (a.isOutAdapter())
				srcElemCombo.add(item.getId());
			else {
				srcElemCombo.add(item.getId());
				dstElemCombo.add(item.getId());
			}
		}

		// Port
		Label label5 = new Label(container, SWT.WRAP);
		label5.setText("Port");
		label5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Source port Combo
		srcPortCombo = new Combo(container, SWT.NONE);
		srcPortCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Destination port Combo
		dstPortCombo = new Combo(container, SWT.NONE);
		dstPortCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		// Listeners
		srcElemCombo.addModifyListener(iListener);
		dstElemCombo.addModifyListener(iListener);
		srcPortCombo.addModifyListener(iListener);
		dstPortCombo.addModifyListener(iListener);
		srcElemCombo.addModifyListener(new ComboUpdate(srcElemCombo, srcPortCombo, OUT_PORT));
		dstElemCombo.addModifyListener(new ComboUpdate(dstElemCombo, dstPortCombo, IN_PORT));

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
		return new Point(550, 250);
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

			for (Component i : chain.getComponents()) {
				if (i.getId().equalsIgnoreCase(comboElem.getText())) {
					if (i instanceof AdapterInstance) {
						comboPort.setEnabled(false);
						updateResult();
						return;
					} else {
						comboPort.setEnabled(true);
						fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent m = JarRepoService
								.getInstance().getMediator(i.getReferencedTypeID());
						if (m != null) {
							for (JarPort p : m.getPorts()) {
								if (p instanceof InPort && portType.equals(IN_PORT))
									comboPort.add(p.getName());
								if (p instanceof OutPort && portType.equals(OUT_PORT))
									comboPort.add(p.getName());
							}
						}
						updateResult();
						return;
					}
				}
			}
			updateResult();
			return;
		}
	}
}
