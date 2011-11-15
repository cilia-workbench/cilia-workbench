package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.CommonHandler;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

/**
 * Handler used when deleting a chain in the chain designer view.
 * 
 * @author Etienne Gandrille
 */
public class DeleteBindingHandler extends CommonHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Chain chain = getChainDesignerView(event).getModel();
		if (chain != null) {
			DeleteBindingWindow window = new DeleteBindingWindow(getShell(event), chain);
			if (window.open() == Window.OK) {
				String srcElem = window.getSrc();
				String dstElem = window.getDst();

				try {
					DsciliaRepoService.getInstance().deleteBinding(chain, srcElem, dstElem);
				} catch (MetadataException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
