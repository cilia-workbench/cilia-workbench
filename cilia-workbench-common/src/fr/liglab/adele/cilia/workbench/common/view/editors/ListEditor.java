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
package fr.liglab.adele.cilia.workbench.common.view.editors;

import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A simple list<String> editor, with an add an a remove button.
 * 
 * @author Etienne Gandrille
 */
public class ListEditor extends AbstractEditor {

	/** the list contents */
	private List<String> input;

	private Text text;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent composite.
	 * @param input
	 *            the input, which can be an empty list, but mustn't be null.
	 */
	public ListEditor(Composite parent, List<String> input) {
		super(parent);

		this.input = input;

		// Composite
		widgetComposite = new Composite(parent, SWT.NONE);
		widgetComposite.setLayout(new GridLayout(2, false));

		// Text
		text = new Text(widgetComposite, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Add Button
		final Button btnAdd = new Button(widgetComposite, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnAdd.setText(addLabel);

		// JFace List Viewer
		jFaceViewer = new ListViewer(widgetComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		jFaceViewer.setContentProvider(new ArrayContentProvider());
		jFaceViewer.setLabelProvider(new LabelProvider());
		jFaceViewer.setInput(input);
		jFaceViewer.setComparator(getDefaultComparator());
		jFaceViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Remove Button
		final Button btnRemove = new Button(widgetComposite, SWT.NONE);
		btnRemove.setText(removeLabel);
		btnRemove.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		// Listeners
		btnAdd.addMouseListener(new AddButtonListener(shell, text, jFaceViewer));
		btnRemove.addMouseListener(new RemoveButtonListener(shell, jFaceViewer));
	}

	/**
	 * Listener called when clicking on the Add Button.
	 */
	private class AddButtonListener implements MouseListener {

		private Text text;
		private Shell shell;
		private StructuredViewer list;

		public AddButtonListener(Shell shell, Text text, StructuredViewer list) {
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
				MessageDialog.openError(shell, "Error", "Can't add an empty string");
			} else {
				boolean found = false;
				for (int i = 0; i < input.size() && !found; i++) {
					if (input.get(i).equalsIgnoreCase(str))
						found = true;
				}

				if (found) {
					MessageDialog.openError(shell, "Error", "\"" + str + "\"" + " is already in the list.");
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
		private StructuredViewer list;

		public RemoveButtonListener(Shell shell, StructuredViewer list) {
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
				MessageDialog.openError(shell, "Error", "Please select an element first.");
			else
				input.remove(str);
			list.refresh();
		}
	}

	@Override
	public void refresh() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.liglab.adele.cilia.dialog.editor.AbstractEditor#getDefaultComparator()
	 */
	@Override
	protected ViewerComparator getDefaultComparator() {
		return new ViewerComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toUpperCase().compareTo(o2.toUpperCase());
			}
		});
	}
}
