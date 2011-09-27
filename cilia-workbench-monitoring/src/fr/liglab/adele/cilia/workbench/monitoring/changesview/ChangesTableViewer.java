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
package fr.liglab.adele.cilia.workbench.monitoring.changesview;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The table viewer, used in the changes view.
 * This class is a singleton. Only one viewer can be created.
 */
public class ChangesTableViewer {

	/** The viewer itself. */
	private static TableViewer viewer = null;
	
	/** The table sorter. */
	private static ChangesTableSorterComparator tableSorter = new ChangesTableSorterComparator();
		
	private ChangesTableViewer() {
		// Constructor is not available.
	}

	/**
	 * Gets the singleton viewer.
	 *
	 * @param parent the parent
	 * @return the viewer
	 */
	public static TableViewer getViewer(Composite parent) {
		if (viewer == null)
			createViewer(parent);
		return viewer;
	}
		
	/**
	 * Creates the viewer.
	 *
	 * @param parent the parent
	 */
	private static void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER); 
		viewer.setContentProvider(new ArrayContentProvider());
		
		viewer.setComparator(tableSorter);
		
		createColumnsAndLabelProvider(viewer);
		
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
				
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	/**
	 * Creates the columns and label provider.
	 *
	 * @param viewer the viewer
	 */
	private static void createColumnsAndLabelProvider(final TableViewer viewer) {
		for (ChangesTableColumnHeader columnHeader : ChangesTableColumnHeader.values())
			createTableViewerColumn(viewer, columnHeader);
	}

	/**
	 * Creates the table viewer column.
	 *
	 * @param viewer the viewer
	 * @param columnHeader the column header
	 * @return the table viewer column
	 */
	private static TableViewerColumn createTableViewerColumn(TableViewer viewer, ChangesTableColumnHeader columnHeader) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		viewerColumn.setLabelProvider(columnHeader.getCellLabelProvider());
		final TableColumn column = viewerColumn.getColumn();
		column.setText(columnHeader.getColumnName());
		column.setWidth(100);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, columnHeader));
		return viewerColumn;
	}
	
	/**
	 * Gets the selection adapter.
	 *
	 * @param column the column
	 * @param columnHeader the column header
	 * @return the selection adapter
	 */
	private static SelectionAdapter getSelectionAdapter(final TableColumn column,
			final ChangesTableColumnHeader columnHeader) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableSorter.setColumn(columnHeader);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}
}
