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
package fr.liglab.adele.cilia.workbench.monitoring.changesview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import fr.liglab.adele.cilia.management.BookMark;

/**
 * The Class ChangesTableSorterComparator.
 * @author Etienne Gandrille
 */
public class ChangesTableSorterComparator extends ViewerComparator {
	
	/** The default column for sorting. */
	private final ChangesTableColumnHeader defaultKey = ChangesTableColumnHeader.SEQ_NUMBER;  
	
	/** The current column for sorting. */
	private ChangesTableColumnHeader sortKey = defaultKey;
	
	/** The default direction. */
	private final int defaultDirection = SWT.UP; 
	
	/** The current direction. */
	private int direction = defaultDirection;

	public ChangesTableSorterComparator() {
	}

	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the column used for sorting.
	 * If the column is already set, reverse the sorting order.
	 *
	 * @param columnHeader the column.
	 */
	public void setColumn(ChangesTableColumnHeader columnHeader) {
		if (columnHeader.equals(this.sortKey)) {
			swapDirection();
		} else {
			this.sortKey = columnHeader;
			resetDirection();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
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
	
	/**
	 * Swap direction.
	 */
	private void swapDirection() {
		if (direction == SWT.DOWN)
			direction = SWT.UP;
		else
			direction = SWT.DOWN;
	}
	
	/**
	 * Reset direction.
	 */
	private void resetDirection() {
		direction = defaultDirection;
	}
}
