package fr.liglab.adele.cilia.workbench.common.view;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.handlers.HandlerUtil;

public class ViewUtil {
	
	public static IViewPart findViewWithId(ExecutionEvent event, String viewId) {
		IViewReference[] views = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getViewReferences();
		for (IViewReference view : views)
			if (view.getId().equals(viewId))
				return view.getView(true);
		
		throw new RuntimeException("view with id " + viewId + " not found.");
	}
}
