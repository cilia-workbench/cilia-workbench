package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

public class DeleteFileHandler  extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DsciliaRepositoryView view = getRepositoryView(event);
		Object object = view.getFirstSelectedElement();
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		if (object != null && object instanceof RepoElement) {
			RepoElement repo = (RepoElement) object;
			boolean result = MessageDialog.openConfirm(shell, "Confirmation required", "Do you want to delete " + repo.getFilePath());
			if (result == true)
				DsciliaRepoService.getInstance().deleteRepoElement(repo);
		} else {
			MessageDialog.openError(shell, "Error", "You must select a dscila file first.");
		}
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
