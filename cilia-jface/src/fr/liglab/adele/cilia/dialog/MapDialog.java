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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * A utility class used to modify a Map thanks to a modal dialog. Typical
 * interaction is : <code>
 *  MapDialog md = new MapDialog(parent, items);
 *  int retcode = ld.open();
 *  if (retcode == Window.OK)
 *    result = md.getResult();
 *  else
 *    ...  
 *  </code>
 */
public class MapDialog extends Dialog {

	/** The list Viewer widget */
	private TableViewer table;

	/** Used to store the initial and final list */
	private final Map<String, String> input;

	/** Margin used by the GridLayout */
	private final int margin = 10;

	/** Shell title */
	private final String title = "Table editor";

	/** User helper message */
	private final String message = "You can add or remove elements in this table sorted by alphabetical order.";

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Parent shell. Mandatory for this modal dialog.
	 * @param items
	 *            initial map
	 */
	public MapDialog(Shell parent, Map<String, String> items) {
		super(parent);

		this.input = new HashMap<String, String>();
		for (String key : items.keySet())
			this.input.put(key, items.get(key));
	}

	/**
	 * Gets the result. If the users exits on Cancel, returns the initial map.
	 * Of course,this method must be called after a call to open().
	 * 
	 * @return the result.
	 */
	public Map<String, String> getResult() {
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
		final Composite compo = new Composite(container, SWT.NONE);
		compo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		compo.setLayout(new GridLayout(2, false));
		final Text text1 = new Text(compo, SWT.NONE);
		text1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		final Text text2 = new Text(compo, SWT.NONE);
		text2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Add Button
		final Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.setText("Add");

		// JFace Table Viewer
		table = new TableViewer(container, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		final TableViewerColumn col1 = createTableViewerColumn(0, "key");
		final TableViewerColumn col2 = createTableViewerColumn(1, "value");
		table.setContentProvider(new TableContentProvider());
		table.setInput(input);
		table.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		table.setComparator(new KeyValueComparator());

		// SWT Table widget configuration
		Table swtTable = table.getTable();
		swtTable.setHeaderVisible(true);
		swtTable.setLinesVisible(true);

		// Remove Button
		final Button btnRemove = new Button(container, SWT.NONE);
		btnRemove.setText("Remove");
		btnRemove.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));

		// Listeners
		compo.addControlListener(new ResizeListener(col1, col2, compo));
		btnAdd.addMouseListener(new AddButtonListener(container.getShell(),
				text1, text2, table));
		btnRemove.addMouseListener(new RemoveButtonListener(container
				.getShell(), table));
		table.addSelectionChangedListener(new SelectTableListener(text1, text2));

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
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		// items = new ArrayList<String>();
		// for (String item : list.getList().getItems())
		// items.add(item);
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(400, 450);
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
	 * Convert the map into Object[].
	 */
	public class TableContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {

			@SuppressWarnings("unchecked")
			Map<String, String> items = (Map<String, String>) inputElement;

			List<String[]> retval = new ArrayList<String[]>();

			for (String key : items.keySet()) {
				String value = items.get(key);
				String[] keyValue = new String[2];
				keyValue[0] = key;
				keyValue[1] = value;
				retval.add(keyValue);
			}

			return retval.toArray();
		}

		@Override
		public void dispose() {
			// do nothing
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}
	}

	/**
	 * Creates a column in the table viewer.
	 * 
	 * @param index
	 *            the column number
	 * @param title
	 *            the column name
	 * @return the column JFace object
	 */
	private TableViewerColumn createTableViewerColumn(final int index,
			String title) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(table,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		// column.setWidth(bound); // done by resize listener
		column.setResizable(false);
		column.setMoveable(false);
		column.setAlignment(SWT.CENTER);

		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String[] keyValue = (String[]) element;
				return keyValue[index];
			}
		});

		return viewerColumn;
	}

	/**
	 * Resize the table column as soon as a composite is resized.
	 */
	private class ResizeListener implements ControlListener {
		final TableViewerColumn col1;
		final TableViewerColumn col2;
		final Composite composite;

		/**
		 * Constructor.
		 * 
		 * @param col1
		 *            the first column
		 * @param col2
		 *            the second one
		 * @param composite
		 *            the composite to be watched.
		 */
		public ResizeListener(TableViewerColumn col1, TableViewerColumn col2,
				Composite composite) {
			this.col1 = col1;
			this.col2 = col2;
			this.composite = composite;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse
		 * .swt.events.ControlEvent)
		 */
		@Override
		public void controlResized(ControlEvent e) {
			int width = composite.getBounds().width / 2 - 2;
			col1.getColumn().setWidth(width);
			col2.getColumn().setWidth(width);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt
		 * .events.ControlEvent)
		 */
		@Override
		public void controlMoved(ControlEvent e) {
			// do nothing
		}
	}

	/**
	 * Listener called when clicking on the Add Button.
	 */
	private class AddButtonListener implements MouseListener {

		private Text text1;
		private Text text2;
		private Shell shell;
		private TableViewer table;

		public AddButtonListener(Shell shell, Text text1, Text text2,
				TableViewer table) {
			this.shell = shell;
			this.text1 = text1;
			this.text2 = text2;
			this.table = table;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// do nothing
		}

		@Override
		public void mouseDown(MouseEvent e) {
			// do nothing
		}

		@Override
		public void mouseUp(MouseEvent e) {
			String str1 = text1.getText().trim();
			String str2 = text2.getText().trim();

			if (str1.isEmpty()) {
				MessageDialog.openError(shell, "Error",
						"Can't add an empty key");
				text1.setFocus();
			} else if (str2.isEmpty()) {
				MessageDialog.openError(shell, "Error",
						"Can't add an empty value");
				text2.setFocus();
			} else {
				boolean found = input.containsKey(str1);

				if (found) {
					// If the new value is different from the previus one
					if (input.get(str1).compareToIgnoreCase(str2) != 0) {
						String msg = "The key \"" + str1 + "\""
								+ " already exists.\n"
								+ "Do you want to overide the value?\n"
								+ "Old value: " + input.get(str1)
								+ "\nNew value: " + str2;
						boolean retval = MessageDialog.openConfirm(shell,
								"Confirmation", msg);

						if (retval == true) {
							input.remove(str1);
							input.put(str1, str2);
							table.refresh();

							text1.setText("");
							text2.setText("");
							text1.setFocus();
						} else {
							text1.setFocus();
						}
					}

					// The old value is the same as the new value
					else {
						String msg = "The key \"" + str1 + "\""
								+ " already exists with value \"" + str2 + "\"";
						MessageDialog.openInformation(shell, "Info", msg);
					}
				} else {
					input.put(str1, str2);
					table.refresh();

					text1.setText("");
					text2.setText("");
					text1.setFocus();
				}
			}
		}
	}

	/**
	 * Listener called when clicking on the remove button.
	 */
	private class RemoveButtonListener implements MouseListener {

		private Shell shell;
		private TableViewer table;

		public RemoveButtonListener(Shell shell, TableViewer table) {
			this.shell = shell;
			this.table = table;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseUp(MouseEvent e) {
			StructuredSelection sel = (StructuredSelection) table
					.getSelection();
			String[] line = (String[]) sel.getFirstElement();

			if (line == null) {
				MessageDialog.openError(shell, "Error",
						"Please select an element first.");
			} else {
				input.remove(line[0]);
				table.refresh();
			}
		}
	}

	/**
	 * Listener called when an element is selected in the table.
	 */
	public class SelectTableListener implements ISelectionChangedListener {

		private final Text text1;
		private final Text text2;

		public SelectTableListener(Text text1, Text text2) {
			this.text1 = text1;
			this.text2 = text2;
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			StructuredSelection sel = (StructuredSelection) table
					.getSelection();
			String[] line = (String[]) sel.getFirstElement();

			if (line != null) {
				String key = line[0];
				String value = line[1];

				text1.setText(key);
				text2.setText(value);
			}
		}
	}

	private class KeyValueComparator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			String str1 = ((String[]) e1)[0];
			String str2 = ((String[]) e2)[0];

			return (str1.toUpperCase()).compareTo(str2.toUpperCase());
		}
	}
}
