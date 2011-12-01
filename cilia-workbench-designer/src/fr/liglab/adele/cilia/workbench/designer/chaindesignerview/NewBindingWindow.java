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
package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

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

import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.ComponentInstance;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.MediatorInstance;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Adapter;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.InPort;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.OutPort;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Port;

/**
 * Window used for Binding instance creation in a chain.
 * 
 * Should be used as this snippet : <code>
 * NewBindingWindow window = new NewBindingWindow(event, chain);
 * if (window.open() == Window.OK) {
 *   String srcElem = window.getSrcElem();
 *   String dstElem = window.getDstElem();
 *   String srcPort = window.getSrcPort();
 *   String dstPort = window.getDstPort();
 *   (...)
 * }
 * </code>
 * 
 * @author Etienne Gandrille
 */
public class NewBindingWindow extends Dialog {

	/** Used internally */
	private enum PortType {
		IN, OUT
	}

	/** The scope for the binding to be created. */
	private final Chain chain;

	/** Window title. */
	private final String windowTitle = "New binding";

	/** Margin used by the GridLayout. */
	private final int margin = 10;

	/** Combo widget for selecting the source element for this binding. */
	private Combo srcElemCombo;

	/** Combo widget for selecting the destination element for this binding. */
	private Combo dstElemCombo;

	/** Combo widget for selecting the source port (on the source element) for this binding. */
	private Combo srcPortCombo;

	/** Combo widget for selecting the destination port (on the destination element) for this binding. */
	private Combo dstPortCombo;

	/** Message area for displaying error and warning messages. */
	private Label messageArea;

	/** Listener called to check integrity constraints when modifying data in the window. */
	private final IntegrityListener iListener = new IntegrityListener();

	/** Result : source element. */
	private String srcElem;

	/** Result : destination element. */
	private String dstElem;

	/** Result : source port (on the source element). */
	private String srcPort;

	/** Result : source destination (on the destination element). */
	private String dstPort;

	/**
	 * Instantiates a new new binding window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	protected NewBindingWindow(Shell parentShell, Chain chain) {
		super(parentShell);
		Preconditions.checkNotNull(chain);
		Preconditions.checkNotNull(parentShell);
		this.chain = chain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
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
		for (MediatorInstance item : chain.getMediators()) {
			srcElemCombo.add(item.getId());
			dstElemCombo.add(item.getId());
		}
		for (AdapterInstance item : chain.getAdapters()) {
			String id = item.getType();
			Adapter a = JarRepoService.getInstance().getAdapter(id);
			if (a == null) {
				srcElemCombo.add(item.getId());
				dstElemCombo.add(item.getId());
			} else if (a.getPattern().equalsIgnoreCase(Adapter.IN_PATTERN))
				dstElemCombo.add(item.getId());
			else if (a.getPattern().equalsIgnoreCase(Adapter.OUT_PATTERN))
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
		srcElemCombo.addModifyListener(new ComboUpdate(srcElemCombo, srcPortCombo, PortType.OUT));
		dstElemCombo.addModifyListener(new ComboUpdate(dstElemCombo, dstPortCombo, PortType.IN));

		return container;
	}

	/**
	 * Gets the source element for the binding to be created.
	 * 
	 * @return the source element.
	 */
	public String getSrcElem() {
		return srcElem;
	}

	/**
	 * Gets the source destination for the binding to be created.
	 * 
	 * @return the destination element.
	 */
	public String getDstElem() {
		return dstElem;
	}

	/**
	 * Gets the source element port for the binding to be created.
	 * 
	 * @return the source element port.
	 */
	public String getSrcPort() {
		return srcPort;
	}

	/**
	 * Gets the destination element port for the binding to be created.
	 * 
	 * @return the destination element port.
	 */
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
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse .swt.widgets.Composite)
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

	/**
	 * Updates the result with values selected by the user.
	 */
	protected void updateResult() {
		srcElem = srcElemCombo.getText();
		dstElem = dstElemCombo.getText();
		srcPort = srcPortCombo.getText();
		dstPort = dstPortCombo.getText();
	}

	/**
	 * Listener called each time a value is modified. This listener :
	 * <ul>
	 * <li>Updates the message area with error or warning message</li>
	 * <li>Enamble or disable the OK button</li>
	 * <li>Updates the result.</li>
	 * </ul>
	 * 
	 * @see IntegrityEvent
	 * @author Etienne Gandrille
	 */
	private class IntegrityListener implements ModifyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
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

	/**
	 * Listener called each time a value is modified. This listener updates the ports list when the source or
	 * destination element is modified.
	 * 
	 * @author Etienne Gandrille
	 */
	private class ComboUpdate implements ModifyListener {

		/** The combo element. */
		private final Combo comboElem;

		/** The combo port. */
		private final Combo comboPort;

		/** The port type. */
		private final PortType portType;

		/**
		 * Instantiates a new combo update.
		 * 
		 * @param comboElem
		 *            the combo elem
		 * @param comboPort
		 *            the combo port
		 * @param portType
		 *            the port type
		 */
		public ComboUpdate(Combo comboElem, Combo comboPort, PortType portType) {
			this.comboElem = comboElem;
			this.comboPort = comboPort;
			this.portType = portType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		@Override
		public void modifyText(ModifyEvent e) {
			comboPort.removeAll();

			for (ComponentInstance i : chain.getComponents()) {
				if (i.getId().equalsIgnoreCase(comboElem.getText())) {
					if (i instanceof AdapterInstance) {
						comboPort.setEnabled(false);
						updateResult();
						return;
					} else {
						comboPort.setEnabled(true);
						MediatorComponent m = JarRepoService.getInstance().getMediator(i.getType());
						if (m != null) {
							for (Port p : m.getPorts()) {
								if (p instanceof InPort && portType.equals(PortType.IN))
									comboPort.add(p.getName());
								if (p instanceof OutPort && portType.equals(PortType.OUT))
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
