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

import java.util.HashMap;
import java.util.Map;

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

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;

public class DeleteBindingWindow extends Dialog {

	private final Chain chain;
	
	private final String windowTitle = "Delete binding";
	
	private final String message = "Please select the binding to be removed";
	
	private final Map<String, String> srcMap = new HashMap<String, String>();
	
	private final Map<String, String> dstMap = new HashMap<String, String>();
	
	private String src;
	
	private String dst;
	
	/** Margin used by the GridLayout. */
	private final int margin = 10;

	private Combo selector;
			
	protected DeleteBindingWindow(Shell parentShell, Chain chain) {
		super(parentShell);
		Preconditions.checkNotNull(chain);
		Preconditions.checkNotNull(parentShell);
		this.chain = chain;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		getShell().setText(windowTitle);

		// Global layout
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// Message
		Label messageLabel = new Label(container, SWT.WRAP);
		messageLabel.setText(message);
		messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Selector
		selector = new Combo(container, SWT.READ_ONLY);
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Listener
		selector.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selector.getText().equals(""))
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					src = srcMap.get(selector.getText());
					dst = dstMap.get(selector.getText());
				}
			}
		});
		
		// populating
		for (Binding binding : chain.getBindings()) {
			String msg = binding.getSource() + " --> " + binding.getDestination();
			srcMap.put(msg, binding.getSource());
			dstMap.put(msg, binding.getDestination());
			selector.add(msg);
		}
		
		return container;
	}

	
	/* (non-Javadoc)
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
	
	public String getSrc() {
		return src;
	}
	
	public String getDst() {
		return dst;
	}
}
