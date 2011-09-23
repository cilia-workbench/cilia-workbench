package fr.liglab.adele.cilia.workbench.monitoring.changesview;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import fr.liglab.adele.cilia.management.BookMark;


public class ChangesTableSorterComparator extends ViewerComparator {
	
	private final ChangesTableColumnHeader defaultKey = ChangesTableColumnHeader.SEQ_NUMBER;  
	private ChangesTableColumnHeader sortKey = defaultKey;
	
	private final int defaultDirection = SWT.UP; 
	private int direction = defaultDirection;

	public ChangesTableSorterComparator() {
	}

	public int getDirection() {
		return direction;
	}

	public void setColumn(ChangesTableColumnHeader columnHeader) {
		if (columnHeader.equals(this.sortKey)) {
			swapDirection();
		} else {
			this.sortKey = columnHeader;
			resetDirection();
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		BookMark b1 = (BookMark) e1;
		BookMark b2 = (BookMark) e2;
		int rc = sortKey.compare(b1, b2);
		// If descending order, flip the direction
		if (direction == SWT.UP) {
			rc = -rc;
		}
		return rc;
	}
	
	private void swapDirection() {
		if (direction == SWT.DOWN)
			direction = SWT.UP;
		else
			direction = SWT.DOWN;
	}
	
	private void resetDirection() {
		direction = defaultDirection;
	}
}
