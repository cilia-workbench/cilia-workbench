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
package fr.liglab.adele.cilia.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A utility class used to modify a string List thanks to a modal dialog.
 * Typical interaction is : 
 * <code>
 *  ListDialog ld = new ListDialog(parent, items);
 *  int retcode = ld.open();
 *  if (retcode == Window.OK)
 *    result = ld.getResult();
 *  else
 *    ...  
 *  </code>
 *
 * @author Etienne Gandrille
 */
public class ListDialog extends Dialog {

	/** The list Viewer widget */
	private ListViewer list;

	/** Used to store the initial and final list */
	private List<String> input;

	/** Margin used by the GridLayout */
	private final int margin = 10;
	
	/** Shell title */
	private final String title = "List editor";
	
	/** User helper message */
	private final String message = "You can add or remove elements in this list sorted by alphabetical order.";
	
	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Parent shell. Mandatory for this modal dialog.
	 * @param input
	 *            initial list
	 */
	public ListDialog(Shell parent, List<String> input) {
		super(parent);
		this.input = new ArrayList<String>(input);
	}

	/**
	 * Gets the result after the user clicks on OK. Of course,this method must
	 * be called after a call to open(). If the user exit with CANCEL, the
	 * result of this method is unspecified.
	 * 
	 * @return the result.
	 */
	public List<String> getResult() {
		return input;
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
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = margin;
		layout.marginHeight = margin;
		layout.horizontalSpacing = margin;
		layout.verticalSpacing = margin;
		container.setLayout(layout);

		// Label
		final Label label = new Label(container, SWT.WRAP);
		label.setText(message);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		// Text
		final Text text = new Text(container, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Add Button
		final Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.setText("Add");

		// List Viewer
		list = new ListViewer(container, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		list.setContentProvider(new ArrayContentProvider());
		list.setLabelProvider(new LabelProvider());
		list.setInput(input);
		list.setComparator(new ViewerComparator(new StringComparatorIgnoreCase()));
		list.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		// Remove Button
		final Button btnRemove = new Button(container, SWT.NONE);
		btnRemove.setText("Remove");
		btnRemove.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));

		// Listeners
		btnAdd.addMouseListener(new AddButtonListener(container.getShell(),
				text, list));
		btnRemove.addMouseListener(new RemoveButtonListener(container
				.getShell(), list));

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(300, 450);
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
	 * Listener called when clicking on the Add Button.
	 */
	private class AddButtonListener implements MouseListener {

		private Text text;
		private Shell shell;
		private ListViewer list;

		public AddButtonListener(Shell shell, Text text, ListViewer list) {
			this.shell = shell;
			this.text = text;
			this.list = list;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseUp(MouseEvent e) {
			String str = text.getText().trim();

			if (str.isEmpty()) {
				MessageDialog.openError(shell, "Error",
						"Can't add an empty string");
			} else {
				boolean found = false;
				for (int i = 0; i < input.size() && !found; i++) {
					if (input.get(i).equalsIgnoreCase(str))
						found = true;
				}

				if (found) {
					MessageDialog.openError(shell, "Error", "\"" + str + "\""
							+ " is already in the list.");
				} else {
					input.add(str);
					list.refresh();
				}
			}
			text.setText("");
			text.setFocus();
		}
	}

	/**
	 * Listener called when clicking on the remove button.
	 */
	private class RemoveButtonListener implements MouseListener {

		private Shell shell;
		private ListViewer list;

		public RemoveButtonListener(Shell shell, ListViewer list) {
			this.shell = shell;
			this.list = list;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseUp(MouseEvent e) {
			StructuredSelection sel = (StructuredSelection) list.getSelection();
			String str = (String) sel.getFirstElement();

			if (str == null)
				MessageDialog.openError(shell, "Error",
						"Please select an element first.");
			else
				input.remove(str);
				list.refresh();
		}
	}
	
	/**
	 * Comparator, used to sort items in the list.
	 */
	private class StringComparatorIgnoreCase implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return o1.toUpperCase().compareTo(o2.toUpperCase());
		}
	}
}
