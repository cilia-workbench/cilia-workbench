package fr.liglab.adele.cilia.workbench.common.sourceprovider;

import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

public class SourceProviderUtil {
	
	public static void setToggleVariable(String variable, boolean newValue) {
		//ISourceProviderService sourceProviderService = (ISourceProviderService)  getSite().getService();
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		if (sourceProviderService != null) {
			ISourceProvider provider = sourceProviderService
					.getSourceProvider(variable);
			
			if (provider instanceof ToggleSourceProvider) {
				((ToggleSourceProvider) provider).setValue(newValue);
			} else {
				throw new RuntimeException("Provider is not instance of VariablesSourceProvider");
			}
		} else
			throw new RuntimeException("Can't retrieve sourceProviderService");
	}
}
