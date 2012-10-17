package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

public class ReloadHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PlatformRepoService.getInstance().updateModel();		
		return null;
	}
	
}
