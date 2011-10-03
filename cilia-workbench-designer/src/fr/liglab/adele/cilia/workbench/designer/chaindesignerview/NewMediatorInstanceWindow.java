package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;

public class NewMediatorInstanceWindow extends Dialog {

	protected NewMediatorInstanceWindow(Shell parentShell, Chain chain) {
		super(parentShell);
		Preconditions.checkNotNull(chain);
		this.chain = chain;
	}

	private final String windowTitle = "New mediator instance";
	private final String idLabelText = "ID";
	private final String typeLabelText = "Type";

	private Text idText = null;
	private Text typeText = null;
	Label messageArea = null;
	
	private String mediatorId;
	private String mediatorType;

	private final WindowModifyListener listener = new WindowModifyListener();

	private final Chain chain;

	/** Margin used by the GridLayout */
	private final int margin = 10;

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		getShell().setText(windowTitle);

		// Global layout
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// IdLabel
		final Label idLabel = new Label(container, SWT.WRAP);
		idLabel.setText(idLabelText);
		idLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Id Text
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Type Label
		final Label typeLabel = new Label(container, SWT.WRAP);
		typeLabel.setText(typeLabelText);
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Type Text
		typeText = new Text(container, SWT.NONE);
		typeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Message Area
		messageArea = new Label(container, SWT.WRAP);
		messageArea.setText("");
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// Listeners
		idText.addModifyListener(listener);
		typeText.addModifyListener(listener);

		return container;
	}


	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		
	}

	public String getMediatorId() {
		return mediatorId;
	}
	
	public String getType() {
		return mediatorType;
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
		return new Point(300, 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

	private class WindowModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			String id = idText.getText();
			String type = typeText.getText();
			String msg = chain.isNewMediatorInstanceAllowed(id, type);

			messageArea.setText(Strings.nullToEmpty(msg));
			getButton(IDialogConstants.OK_ID).setEnabled(msg == null);
			mediatorId = idText.getText();
			mediatorType = typeText.getText();
		}
	}
}
