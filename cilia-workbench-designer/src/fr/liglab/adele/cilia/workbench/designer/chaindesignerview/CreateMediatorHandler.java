package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.CommonHandler;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

public class CreateMediatorHandler extends CommonHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
			
		Chain chain = getChainDesignerView(event).getModel();
		if (chain != null) {
			NewMediatorInstanceWindow window = new NewMediatorInstanceWindow(getShell(event), chain);
			if (window.open() == Window.OK) {
				String id = window.getComponentId();
				String type = window.getComponentType();
				try {
					DsciliaRepoService.getInstance().createMediatorInstance(chain, id, type);
				} catch (MetadataException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
