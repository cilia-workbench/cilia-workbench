package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;


public class ReloadHandler extends AbstractHandler {
	
	/**
	 * Returns the topology view.
	 * 
	 * @param event
	 * @return
	 */
	protected RepositoryView getRepositoryView(ExecutionEvent event) {
		String viewId = RepositoryView.viewId;		
		return (RepositoryView) ViewUtil.findViewWithId(event, viewId);
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		getRepositoryView(event).reload();
		return null;
	}
}
