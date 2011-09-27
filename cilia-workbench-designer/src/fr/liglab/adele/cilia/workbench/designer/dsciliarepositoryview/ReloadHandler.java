package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;

public class ReloadHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		getRepositoryView(event).refresh();
		return null;
	}

	/**
	 * Gets the Repository View.
	 * 
	 * @param event
	 *            the handler event
	 * @return the RepositoryView
	 */
	private DsciliaRepositoryView getRepositoryView(ExecutionEvent event) {
		String viewId = DsciliaRepositoryView.viewId;
		return (DsciliaRepositoryView) ViewUtil.findViewWithId(event, viewId);
	}
}