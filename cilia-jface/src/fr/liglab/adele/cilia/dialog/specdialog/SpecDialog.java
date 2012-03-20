package fr.liglab.adele.cilia.dialog.specdialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fr.liglab.adele.cilia.dialog.editor.KeyValueEditor;
import fr.liglab.adele.cilia.dialog.editor.ListEditor;
import fr.liglab.adele.cilia.dialog.editor.Utilities;

public class SpecDialog extends Dialog {

	/** Shell title */
	private final String title = "Spec editor";

	/** User helper message */
	private final String introMessage = "You can edit or create a new specification.";

	private final String idMessage = "id";

	private String namespaceMessage = "namespace";

	private String synchroPortsMessage = "In ports:";

	private String dispatchPortsMessage = "Out ports:";

	private String schedulerTitle = "scheduler";

	private String dispatcherTitle = "dispatcher";

	private String processorTitle = "processor";

	private final static String propertiesMessage = "You can add properties for discribing intinsic caracteristics";

	private static int maxPorts = 5;

	private KeyValueEditor editor;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Parent shell. Mandatory for this modal dialog.
	 * @param input
	 *            initial list
	 */
	public SpecDialog(Shell parent) {
		super(parent);
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

		getShell().setText(title);

		// Global layout
		GridLayout mainLayout = new GridLayout(1, false);
		container.setLayout(mainLayout);

		// Main composites
		Composite generalComposite = new Composite(container, SWT.NONE);
		generalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite portsComposite = new Composite(container, SWT.NONE);
		portsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite advancedComposite = new Composite(container, SWT.NONE);
		advancedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// General composite
		// =================

		GridLayout generalLayout = new GridLayout(2, false);
		generalComposite.setLayout(generalLayout);

		// intro
		final Label introLabel = new Label(generalComposite, SWT.WRAP);
		introLabel.setText(introMessage);
		introLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		// id
		createLabel(generalComposite, idMessage, false);
		createText(generalComposite);

		// namespace
		createLabel(generalComposite, namespaceMessage, false);
		createText(generalComposite);

		// Ports composite
		// ===============

		GridLayout portsLayout = new GridLayout(2, false);
		portsComposite.setLayout(portsLayout);

		createLabel(portsComposite, synchroPortsMessage, false);
		createLabel(portsComposite, dispatchPortsMessage, false);

		// In ports
		List<String> inPorts = new ArrayList<String>();

		ListEditor inPortsEditor = new ListEditor(portsComposite, inPorts);
		inPortsEditor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Out Ports
		List<String> outPorts = new ArrayList<String>();

		ListEditor outPortsEditor = new ListEditor(portsComposite, outPorts);
		outPortsEditor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Advanced composite
		// =================
		GridLayout detailsLayout = new GridLayout(1, false);
		advancedComposite.setLayout(detailsLayout);

		CTabFolder folder = new CTabFolder(advancedComposite, SWT.BORDER);

		createMediatorTab(folder, "mediator");
		createSPDTab(folder, schedulerTitle);
		createSPDTab(folder, processorTitle);
		createSPDTab(folder, dispatcherTitle);

		folder.setSelection(0);

		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return container;
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		editor.refresh();
	}

	private static Composite commonCreateTab(CTabFolder folder, String titleText, String labelText) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText(titleText);

		// Composite
		Composite container = new Composite(folder, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);
		item.setControl(container);

		// Label info
		createLabel(container, labelText, true);

		return container;
	}

	private Map<String, String> createMediatorTab(CTabFolder folder, String title) {
		Map<String, String> modelObject = new HashMap<String, String>();

		// Init
		Composite container = commonCreateTab(folder, title, propertiesMessage);

		// Editor
		editor = new KeyValueEditor(container, modelObject);
		editor.setKeyValidator(new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText == null || newText.isEmpty())
					return "Key can't be null";
				else
					return null;
			}
		});
		editor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return modelObject;
	}

	private static List<String> createSPDTab(CTabFolder folder, String title) {
		List<String> modelObject = new ArrayList<String>();

		// Init
		String label = "You can add parameters, for adding variabiliy on the " + title;
		Composite container = commonCreateTab(folder, title, label);

		// Editor
		ListEditor listEditor = new ListEditor(container, modelObject);
		listEditor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return modelObject;
	}

	private static Combo createCombo(Composite container) {
		Combo combo = new Combo(container, SWT.READ_ONLY);
		List<String> l = new ArrayList<String>();
		for (int i = 1; i < maxPorts; i++) {
			String str;
			if (i == 1)
				str = i + " port";
			else
				str = i + " ports";
			l.add(str);
		}
		combo.setItems(l.toArray(new String[0]));
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		combo.select(0);
		return combo;
	}

	private static Text createText(final Composite container) {
		final Text text = new Text(container, SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text.addListener(SWT.Verify, Utilities.getTextValidatorListner());
		return text;
	}

	private static Label createLabel(Composite container, String message, boolean horizontalFill) {
		final Label label = new Label(container, SWT.WRAP);
		label.setText(message);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, horizontalFill, false));
		return label;
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
		return new Point(450, 650);
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
