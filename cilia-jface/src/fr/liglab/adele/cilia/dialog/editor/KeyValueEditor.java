/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.dialog.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * A simple map<String, String> editor.
 * 
 * @author Etienne Gandrille
 */
public class KeyValueEditor extends AbstractEditor {

	/* Main fields */
	/* =========== */

	/** Text field for adding a key */
	private final Text text1;

	/** Text field for adding a value */
	private final Text text2;

	/** The Model object : the Map. */
	private final Map<String, String> input;

	/* Labels */
	/* ====== */

	/** The key label. */
	private final String keyLabel = "key";

	/** The value label. */
	private final String valueLabel = "value";

	/* Listeners */
	/* ========= */

	/** The resize listener. */
	private final ResizeListener resizeListener;

	/* Validators */
	/* ========== */

	/** Validator for validating the key field. */
	private IInputValidator keyValidator = null;

	/** Validator for validating the value field. */
	private IInputValidator valueValidator = null;

	public void setKeyValidator(IInputValidator keyValidator) {
		this.keyValidator = keyValidator;
	}

	public void setValueValidator(IInputValidator valueValidator) {
		this.valueValidator = valueValidator;
	}
	
	public Map<String, String> getMap() {
		return input;
	}
	
	public KeyValueEditor(Composite parent, Map<String, String> input) {
		super(parent);
		
		this.input = input;

		// Composite
		widgetComposite = new Composite(parent, SWT.NONE);
		widgetComposite.setLayout(new GridLayout(3, false));
		
		// Text
		text1 = new Text(widgetComposite, SWT.NONE);
		text1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text2 = new Text(widgetComposite, SWT.NONE);
		text2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Add Button
		final Button btnAdd = new Button(widgetComposite, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnAdd.setText(addLabel);

		// JFace Table Viewer
		jFaceViewer = new TableViewer(widgetComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		jFaceViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		final TableViewerColumn col1 = createTableViewerColumn(jFaceViewer, 0, keyLabel);
		final TableViewerColumn col2 = createTableViewerColumn(jFaceViewer, 1, valueLabel);
		jFaceViewer.setContentProvider(new TableContentProvider());
		jFaceViewer.setInput(input);
		jFaceViewer.setComparator(getDefaultComparator());

		// SWT Table widget configuration
		Table swtTable = ((TableViewer) jFaceViewer).getTable();
		swtTable.setHeaderVisible(true);
		swtTable.setLinesVisible(true);

		// Remove Button
		final Button btnRemove = new Button(widgetComposite, SWT.NONE);
		btnRemove.setText(removeLabel);
		btnRemove.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		// Listeners
		btnAdd.addMouseListener(new AddButtonListener(text1, text2, jFaceViewer));
		resizeListener = new ResizeListener(col1, col2, jFaceViewer);
		widgetComposite.addControlListener(resizeListener);
		jFaceViewer.addSelectionChangedListener(new SelectTableListener(text1, text2));
		btnRemove.addMouseListener(new RemoveButtonListener(parent.getShell(), jFaceViewer));
	}

	/**
	 * Convert the map into Object[].
	 */
	private static class TableContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}
	}

	/**
	 * Creates a column in the table viewer.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param index
	 *            the column number
	 * @param title
	 *            the column name
	 * @return the column JFace object
	 */
	private static TableViewerColumn createTableViewerColumn(StructuredViewer viewer, final int index, String title) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(((TableViewer) viewer), SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
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
	 * The listener interface for receiving addButton events. The class that is
	 * interested in processing a addButton event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addAddButtonListener<code> method. When
	 * the addButton event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see AddButtonEvent
	 */
	private class AddButtonListener implements MouseListener {

		/** The text1. */
		private Text text1;

		/** The text2. */
		private Text text2;

		/** The table. */
		private StructuredViewer table;

		/**
		 * Instantiates a new adds the button listener.
		 * 
		 * @param text1
		 *            the text1
		 * @param text2
		 *            the text2
		 * @param table
		 *            the table
		 */
		public AddButtonListener(Text text1, Text text2, StructuredViewer table) {
			this.text1 = text1;
			this.text2 = text2;
			this.table = table;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
		 * .swt.events.MouseEvent)
		 */
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e) {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		@Override
		public void mouseUp(MouseEvent e) {
			String str1 = text1.getText().trim();
			String str2 = text2.getText().trim();

			if (keyValidator != null) {
				String msg = keyValidator.isValid(str1);
				if (msg != null) {
					String title = "Invalid \"" + keyLabel + "\" value";
					MessageDialog.openError(shell, title, msg);
					text1.setFocus();
					return;
				}
			}

			if (valueValidator != null) {
				String msg = valueValidator.isValid(str1);
				if (msg != null) {
					String title = "Invalid \"" + valueLabel + "\" value";
					MessageDialog.openError(shell, title, msg);
					text2.setFocus();
					return;
				}
			}
			
			input.remove(str1);
			input.put(str1, str2);
			table.refresh();

			text1.setText("");
			text2.setText("");
			text1.setFocus();
		}
	}

	/**
	 * Resize the table column as soon as a composite is resized.
	 * 
	 * @see ResizeEvent
	 */
	private class ResizeListener implements ControlListener {

		/** The col1. */
		final TableViewerColumn col1;

		/** The col2. */
		final TableViewerColumn col2;

		/** The table. */
		final StructuredViewer table;

		/**
		 * Constructor.
		 * 
		 * @param col1
		 *            the first column
		 * @param col2
		 *            the second one
		 * @param table
		 *            the table
		 */
		public ResizeListener(TableViewerColumn col1, TableViewerColumn col2, StructuredViewer table) {
			this.col1 = col1;
			this.col2 = col2;
			this.table = table;
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
			Control control = table.getControl();
			int width = control.getBounds().width / 2 - 2;
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
	 * Listener called when an element is selected in the table.
	 * 
	 * @see SelectTableEvent
	 */
	public class SelectTableListener implements ISelectionChangedListener {

		/** The text1. */
		private final Text text1;

		/** The text2. */
		private final Text text2;

		/**
		 * Instantiates a new select table listener.
		 * 
		 * @param text1
		 *            the text1
		 * @param text2
		 *            the text2
		 */
		public SelectTableListener(Text text1, Text text2) {
			this.text1 = text1;
			this.text2 = text2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
		 * (org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			StructuredSelection sel = (StructuredSelection) jFaceViewer.getSelection();
			String[] line = (String[]) sel.getFirstElement();

			if (line != null) {
				String key = line[0];
				String value = line[1];

				text1.setText(key);
				text2.setText(value);
			}
		}
	}

	/**
	 * Listener called when clicking on the remove button.
	 * 
	 * @see RemoveButtonEvent
	 */
	private class RemoveButtonListener implements MouseListener {

		/** The shell. */
		private Shell shell;

		/** The table. */
		private StructuredViewer table;

		/**
		 * Instantiates a new removes the button listener.
		 * 
		 * @param shell
		 *            the shell
		 * @param table
		 *            the table
		 */
		public RemoveButtonListener(Shell shell, StructuredViewer table) {
			this.shell = shell;
			this.table = table;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
		 * .swt.events.MouseEvent)
		 */
		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		@Override
		public void mouseUp(MouseEvent e) {
			StructuredSelection sel = (StructuredSelection) table.getSelection();
			String[] line = (String[]) sel.getFirstElement();

			if (line == null) {
				MessageDialog.openError(shell, "Error", "Please select an element first.");
			} else {
				input.remove(line[0]);
				table.refresh();
			}
		}
	}

	/**
	 * Refresh the viewer.
	 */
	public void refresh() {
		resizeListener.controlResized(null);
	}

	@Override
	protected ViewerComparator getDefaultComparator() {
		return new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				String str1 = ((String[]) e1)[0];
				String str2 = ((String[]) e2)[0];
				return (str1.toUpperCase()).compareTo(str2.toUpperCase());
			}
		};
	}
}
