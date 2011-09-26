package fr.liglab.adele.cilia.workbench.monitoring.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;

import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;

public abstract class CommonTopologyViewHandler extends AbstractHandler {
	
	/**
	 * Returns the topology view.
	 * 
	 * @param event
	 * @return
	 */
	protected TopologyView getTopologyView(ExecutionEvent event) {
		String viewId = TopologyView.viewId;		
		return (TopologyView) ViewUtil.findViewWithId(event, viewId);
	}
}
