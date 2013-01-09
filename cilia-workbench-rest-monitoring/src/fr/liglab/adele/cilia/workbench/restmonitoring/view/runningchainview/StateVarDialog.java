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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class StateVarDialog extends WorkbenchDialog {

	private final static String title = "State Variables";
	private final static Point initialSize = new Point(300, 400);

	private TableViewer viewer;

	protected StateVarDialog(Shell parentShell) {
		super(parentShell, title, initialSize, true, false);
	}

	public Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		container.setLayout(new GridLayout(1, false));

		// Empty element
		Label label = new Label(container, SWT.WRAP);
		label.setText("Available state variables:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// viewer
		viewer = createViewer(container);

		return container;
	}

	private TableViewer createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		createColumns(parent, viewer);
		/*
		 * final Table table = viewer.getTable(); table.setHeaderVisible(true);
		 * table.setLinesVisible(true);
		 * 
		 * viewer.setContentProvider(new ArrayContentProvider()); // Get the
		 * content for the viewer, setInput will call getElements in the //
		 * contentProvider viewer.setInput(ModelProvider.INSTANCE.getPersons());
		 * // Make the selection available to other views
		 * getSite().setSelectionProvider(viewer); // Set the sorter for the
		 * table
		 * 
		 * // Layout the viewer GridData gridData = new GridData();
		 * gridData.verticalAlignment = GridData.FILL; gridData.horizontalSpan =
		 * 2; gridData.grabExcessHorizontalSpace = true;
		 * gridData.grabExcessVerticalSpace = true; gridData.horizontalAlignment
		 * = GridData.FILL; viewer.getControl().setLayoutData(gridData);
		 */
		return viewer;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "name", "enabled", "value" };
		int[] bounds = { 100, 100, 100 };

		// / TODO Continue here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		/*
		 * // First column is for the first name TableViewerColumn col =
		 * createTableViewerColumn(titles[0], bounds[0], 0);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 * 
		 * @Override public String getText(Object element) { Person p = (Person)
		 * element; return p.getFirstName(); } });
		 * 
		 * // Second column is for the last name col =
		 * createTableViewerColumn(titles[1], bounds[1], 1);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 * 
		 * @Override public String getText(Object element) { Person p = (Person)
		 * element; return p.getLastName(); } });
		 * 
		 * // Now the gender col = createTableViewerColumn(titles[2], bounds[2],
		 * 2); col.setLabelProvider(new ColumnLabelProvider() {
		 * 
		 * @Override public String getText(Object element) { Person p = (Person)
		 * element; return p.getGender(); } });
		 * 
		 * // // Now the status married col = createTableViewerColumn(titles[3],
		 * bounds[3], 3); col.setLabelProvider(new ColumnLabelProvider() {
		 * 
		 * @Override public String getText(Object element) { return null; }
		 * 
		 * @Override public Image getImage(Object element) { if (((Person)
		 * element).isMarried()) { return CHECKED; } else { return UNCHECKED; }
		 * } });
		 * 
		 * }
		 * 
		 * private TableViewerColumn createTableViewerColumn(String title, int
		 * bound, final int colNumber) { final TableViewerColumn viewerColumn =
		 * new TableViewerColumn(viewer, SWT.NONE); final TableColumn column =
		 * viewerColumn.getColumn(); column.setText(title);
		 * column.setWidth(bound); column.setResizable(true);
		 * column.setMoveable(true); return viewerColumn;
		 */
	}

	/** * Passing the focus request to the viewer's control. */

	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
