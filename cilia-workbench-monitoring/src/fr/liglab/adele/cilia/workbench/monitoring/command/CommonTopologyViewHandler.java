package fr.liglab.adele.cilia.workbench.monitoring.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;

public abstract class CommonTopologyViewHandler extends AbstractHandler {
	
	/**
	 * Returns the topology view, using the event given in the execute handler callback.
	 * 
	 * @param event
	 * @return
	 */
	protected TopologyView getTopologyView(ExecutionEvent event) {
		String viewId = TopologyView.viewId;
		
		IViewReference[] views = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getViewReferences();
		for (IViewReference view : views)
			if (view.getId().equals(viewId))
				return (TopologyView) view.getView(true);
		
		throw new RuntimeException("view with id " + viewId + " not found.");
	}
}
