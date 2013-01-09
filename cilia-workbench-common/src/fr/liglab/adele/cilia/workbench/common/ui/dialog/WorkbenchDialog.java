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
package fr.liglab.adele.cilia.workbench.common.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Base class for implementing dialogs. A basic configuration is performed by
 * this base class: size, buttons (OK and CANCEL),...
 * 
 * @author Etienne Gandrille
 */
public abstract class WorkbenchDialog extends Dialog {

	private final Point initialSize;
	private final String title;
	private final boolean isOkButtonEnable;
	private final boolean hasCancelButton;

	protected WorkbenchDialog(Shell parentShell, String title, Point initialSize, boolean isOkButtonEnable) {
		this(parentShell, title, initialSize, isOkButtonEnable, true);
	}

	protected WorkbenchDialog(Shell parentShell, String title, Point initialSize, boolean isOkButtonEnable, boolean hasCancelButton) {
		super(parentShell);
		this.initialSize = initialSize;
		this.title = title;
		this.isOkButtonEnable = isOkButtonEnable;
		this.hasCancelButton = hasCancelButton;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control retval = super.createDialogArea(parent);
		getShell().setText(title);
		return retval;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		if (hasCancelButton)
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Point getInitialSize() {
		return initialSize;
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getButton(IDialogConstants.OK_ID).setEnabled(isOkButtonEnable);
	}
}
