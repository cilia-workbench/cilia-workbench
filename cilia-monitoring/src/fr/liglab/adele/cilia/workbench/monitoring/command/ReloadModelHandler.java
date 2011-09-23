package fr.liglab.adele.cilia.workbench.monitoring.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ReloadModelHandler extends CommonTopologyViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		getTopologyView(event).reloadModel();
		return null;
	}
}
