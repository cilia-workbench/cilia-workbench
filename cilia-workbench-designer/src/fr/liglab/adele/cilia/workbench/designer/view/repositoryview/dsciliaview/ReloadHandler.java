package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

public class ReloadHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DSCiliaRepoService.getInstance().updateModel();
		return null;
	}
}
