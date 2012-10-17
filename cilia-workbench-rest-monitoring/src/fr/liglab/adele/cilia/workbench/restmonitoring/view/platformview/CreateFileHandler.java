package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

public class CreateFileHandler extends PlatformViewHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		NewPlatformDialog dialog = new NewPlatformDialog(ViewUtil.getShell(event));
		
		// Dialog creation
		if (dialog.open() == Window.OK) {
			String values[] = dialog.getValues();
			
			String name = values[0];
			String host = values[1];
			String port = values[2];
			
			return PlatformRepoService.getInstance().createFile(name, host, port);
		}

		return null;
	}
}