package fr.liglab.adele.cilia.workbench.monitoring.command;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class ToggleAutoReloadHandler extends CommonTopologyViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// retrieving command
		Command command = event.getCommand();

		// getting value and updating state
		boolean oldValue = HandlerUtil.toggleCommandState(command);
		boolean newValue = !oldValue;
		
		// updating model
		if (newValue == true)
			getTopologyView(event).reloadModel();
		
		return null;
	}
}
