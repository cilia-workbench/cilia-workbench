package fr.liglab.adele.cilia.workbench.monitoring.changesview;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import fr.liglab.adele.cilia.management.BookMark;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;

public enum ChangesTableColumnHeader {

	SEQ_NUMBER("Seq Number", new SeqNumberComparator()),
	TIME("Time", new TimeComparator()),
	EVENT("Event", new EventComparator()),
	ID("Id", new IdComparator()),
	COMMENT("Comment", new CommentComparator());

	/** The name displayed at the front of the column. */
	private final String columnName;
	
	
	private final BookmarkViewerHelper viewerHelper;
	private CellLabelProvider clp = null;

	ChangesTableColumnHeader(String columnName, BookmarkViewerHelper viewerHelper) {
		this.columnName = columnName;
		this.viewerHelper = viewerHelper;
	}

	public String getColumnName() {
		return columnName;
	}

	/**
	 * The compare method, used to sort bookmarks according to the column.
	 */
	public int compare(BookMark b1, BookMark b2) {
		return viewerHelper.compare(b1, b2);
	}

	/**
	 * Gets the label provider, used to display a bookmark field. 
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
	
	private interface BookmarkViewerHelper {
		public int compare(BookMark b1, BookMark b2);
		public String getText(BookMark element);
	}

	
	/* ======= */
	/* IMPLEMS */
	/* ======= */

	private static class SeqNumberComparator implements BookmarkViewerHelper {
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getSequenceNumber() - b1.getSequenceNumber();
		}

		@Override
		public String getText(BookMark element) {
			return Integer.toString(element.getSequenceNumber());
		}
	}

	private static class TimeComparator implements BookmarkViewerHelper {
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return Long.signum(b2.getTimeMs() - b1.getTimeMs());
		}

		@Override
		public String getText(BookMark element) {
			SimpleDateFormat sdf = new SimpleDateFormat(CiliaUtil.DATE_FORMAT);
			String currentTime = sdf.format(element.getTimeMs());
			return currentTime;
		}
	}

	private static class EventComparator implements BookmarkViewerHelper {
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getEventString().compareTo(b1.getEventString());
		}

		@Override
		public String getText(BookMark element) {
			return element.getEventString();
		}
	}

	private static class IdComparator implements BookmarkViewerHelper {
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.getQualifiedId().compareTo(b1.getQualifiedId());
		}

		@Override
		public String getText(BookMark element) {
			return element.getQualifiedId();
		}
	}

	private static class CommentComparator implements BookmarkViewerHelper {
		@Override
		public int compare(BookMark b1, BookMark b2) {
			return b2.toString().compareTo(b1.toString());
		}

		@Override
		public String getText(BookMark element) {
			return element.toString();
		}
	}
}
