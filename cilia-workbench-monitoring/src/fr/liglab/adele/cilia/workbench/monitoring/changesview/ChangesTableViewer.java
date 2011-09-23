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



public class ChangesTableViewer {

	private static TableViewer viewer = null;
	
	private static ChangesTableSorterComparator tableSorter = new ChangesTableSorterComparator();
		
	private ChangesTableViewer() {
		// Constructor is not available.
	}

	public static TableViewer getViewer(Composite parent) {
		if (viewer == null)
			createViewer(parent);
		return viewer;
	}
		
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
	
	private static void createColumnsAndLabelProvider(final TableViewer viewer) {
		for (ChangesTableColumnHeader columnHeader : ChangesTableColumnHeader.values())
			createTableViewerColumn(viewer, columnHeader);
	}

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
