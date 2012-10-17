package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeleteFileHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return deleteFile(event);
	}
}
