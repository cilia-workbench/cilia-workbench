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

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import fr.liglab.adele.cilia.management.BookMark;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;

/**
 * This enum represents the column header.
 */
public enum ChangesTableColumnHeader {

	/** Sequence number column header. */
	SEQ_NUMBER("Seq Number", new SeqNumberComparator()),
	
	/** Time column header. */
	TIME("Time", new TimeComparator()),
	
	/** event column header. */
	EVENT("Event", new EventComparator()),
	
	/** ID column header. */
	ID("Id", new IdComparator()),
	
	/** Comment column header. */
	COMMENT("Comment", new CommentComparator());

	/** The name displayed at the front of the column. */
	private final String columnName;
	
	/** The viewer helper. */
	private final BookmarkViewerHelper viewerHelper;
	
	/** The cell label provider. */
	private CellLabelProvider clp = null;

	/**
	 * Instantiates a new changes table column header.
	 *
	 * @param columnName the column name
	 * @param viewerHelper the viewer helper
	 */
	ChangesTableColumnHeader(String columnName, BookmarkViewerHelper viewerHelper) {
		this.columnName = columnName;
		this.viewerHelper = viewerHelper;
	}

	/**
	 * Gets the column name.
	 *
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * The compare method, used to sort bookmarks according to the column.
	 *
	 * @param b1 the b1
	 * @param b2 the b2
	 * @return the int
	 */
	public int compare(BookMark b1, BookMark b2) {
		return viewerHelper.compare(b1, b2);
	}

	/**
	 * Gets the label provider, used to display a bookmark field.
	 *
	 * @return the cell label provider
	 */
	public CellLabelProvider getCellLabelProvider() {
		final ChangesTableColumnHeader currentEnum = this;
		if (clp == null) {
			clp = new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					BookMark b = (BookMark) element;
					return currentEnum.viewerHelper.getText(b);
				}
			};
		}
		
		return clp;
	}

	
	/* ============================== */
	/* BookmarkViewerHelper Interface */
	/* ============================== */
	
	/**
	 * The Interface BookmarkViewerHelper.
	 */
	private interface BookmarkViewerHelper {
		
		/**
		 * Compare.
		 *
		 * @param b1 the b1
		 * @param b2 the b2
		 * @return the int
		 */
		public int compare(BookMark b1, BookMark b2);
		
		/**
		 * Gets the text.
		 *
		 * @param element the element
		 * @return the text
		 */
		public String getText(BookMark element);
	}

	
	/* ======= */
	/* IMPLEMS */
	/* ======= */

	/**
	 * The Class SeqNumberComparator.
	 */
	private static class SeqNumberComparator implements BookmarkViewerHelper {
		
		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#compare(fr.liglab.adele.cilia.management.BookMark, fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getSequenceNumber() - b1.getSequenceNumber();
		}

		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#getText(fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public String getText(BookMark element) {
			return Integer.toString(element.getSequenceNumber());
		}
	}

	/**
	 * The Class TimeComparator.
	 */
	private static class TimeComparator implements BookmarkViewerHelper {
		
		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#compare(fr.liglab.adele.cilia.management.BookMark, fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return Long.signum(b2.getTimeMs() - b1.getTimeMs());
		}

		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#getText(fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public String getText(BookMark element) {
			SimpleDateFormat sdf = new SimpleDateFormat(CiliaUtil.DATE_FORMAT);
			String currentTime = sdf.format(element.getTimeMs());
			return currentTime;
		}
	}

	/**
	 * The Class EventComparator.
	 */
	private static class EventComparator implements BookmarkViewerHelper {
		
		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#compare(fr.liglab.adele.cilia.management.BookMark, fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getEventString().compareTo(b1.getEventString());
		}

		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#getText(fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public String getText(BookMark element) {
			return element.getEventString();
		}
	}

	/**
	 * The Class IdComparator.
	 */
	private static class IdComparator implements BookmarkViewerHelper {
		
		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#compare(fr.liglab.adele.cilia.management.BookMark, fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getQualifiedId().compareTo(b1.getQualifiedId());
		}

		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#getText(fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public String getText(BookMark element) {
			return element.getQualifiedId();
		}
	}

	/**
	 * The Class CommentComparator.
	 */
	private static class CommentComparator implements BookmarkViewerHelper {
		
		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#compare(fr.liglab.adele.cilia.management.BookMark, fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.toString().compareTo(b1.toString());
		}

		/* (non-Javadoc)
		 * @see fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesTableColumnHeader.BookmarkViewerHelper#getText(fr.liglab.adele.cilia.management.BookMark)
		 */
		@Override
		public String getText(BookMark element) {
			return element.toString();
		}
	}
}
