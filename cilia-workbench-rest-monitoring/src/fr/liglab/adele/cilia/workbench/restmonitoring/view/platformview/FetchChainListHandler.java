package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.HTTPhelper;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.HttpResquestResult;

public class FetchChainListHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Validity checking
		// =================
		
		Object element = getFirstSelectedElementInRepositoryView(event);
		
		if (element == null || ! (element instanceof PlatformFile)) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Please select a platform first");
			return null;
		}
		
		PlatformModel model = ((PlatformFile) element).getModel();
		if (model == null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Model is in a non valid state");
			return null;
		}

		String host = model.getHost();
		String port = model.getPort();
		
		String hostMsg = PlatformModel.hostValidator(host);
		String portMsg = PlatformModel.portValidator(port);
		
		if (hostMsg != null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", hostMsg);
			return null;
		}
		
		if (portMsg != null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", portMsg);
			return null;
		}

		// REST 
		// ====

		HTTPhelper http = new HTTPhelper(host, Integer.valueOf(port));
		HttpResquestResult response = null;
		try {
			response = http.get("/cilia");
		} catch (CiliaException e) {
			e.printStackTrace();
			MessageDialog.openError(ViewUtil.getShell(event), "Can't join " + host + ":" + port, e.getMessage());
			return null;
		}
		try {
			http.close();
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	
		if (response.getStatus().getStatusCode() != 200) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Can't get list on server. Status code: " + response.getStatus().getStatusCode());
			return null;
		}
		
		String json = response.getMessage();
		
		MessageDialog.openInformation(ViewUtil.getShell(event), "Go!", response.getStatus() + "\n" + json);
		
		return null;
	}
}
