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
package fr.liglab.adele.cilia.workbench.designer.view.specrepositoryview;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.view.TextValidatorListener;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewMediatorDialog extends Dialog {

	private final String title = "New mediator specification";

	private final String introMessage = "Please give an id and a namespace for the new mediator specification.";

	private final String idLabelText = "id";

	private final String namespaceLabelText = "namespace";

	private Text idText = null;

	private Text namespaceText = null;

	private Label errorLabel = null;

	private String id;

	private String namespace;

	private final SpecRepoService repoService;

	public NewMediatorDialog(Shell parent) {
		super(parent);
		repoService = SpecRepoService.getInstance();
	}

	public String getId() {
		return id;
	}

	public String getNamespace() {
		return namespace;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		getShell().setText(title);

		// Global layout
		GridLayout mainLayout = new GridLayout(2, false);
		container.setLayout(mainLayout);

		// intro
		final Label introLabel = new Label(container, SWT.WRAP);
		introLabel.setText(introMessage);
		introLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		// id
		final Label idLabel = new Label(container, SWT.WRAP);
		idLabel.setText(idLabelText);
		idLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		idText = new Text(container, SWT.WRAP);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		idText.addListener(SWT.Verify, TextValidatorListener.getTextValidatorListner());
		idText.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onTextUpdate();
			}
		});

		// namespace
		final Label namespaceLabel = new Label(container, SWT.WRAP);
		namespaceLabel.setText(namespaceLabelText);
		namespaceLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		namespaceText = new Text(container, SWT.WRAP);
		namespaceText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		namespaceText.addListener(SWT.Verify, TextValidatorListener.getTextValidatorListner());
		namespaceText.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onTextUpdate();
			}
		});

		// error message area
		errorLabel = new Label(container, SWT.WRAP);
		errorLabel.setText("");
		errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		return container;
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

	private void onTextUpdate() {
		id = idText.getText();
		namespace = namespaceText.getText();

		String message = repoService.isNewMediatorSpecAllowed(new NameNamespace(id, namespace));
		if (message == null) {
			errorLabel.setText("");
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		} else {
			errorLabel.setText(message);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
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
		return new Point(450, 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}
}
