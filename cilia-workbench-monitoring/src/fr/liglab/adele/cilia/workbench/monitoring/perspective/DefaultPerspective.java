package fr.liglab.adele.cilia.workbench.monitoring.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview.ChainView;

public class DefaultPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Editors are placed for free.
		String editorArea = layout.getEditorArea();

		// Left folder
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.20, editorArea);
		left.addView(TopologyView.viewId);
				
		
		// Top folder
		IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, (float) 0.20, editorArea);
		top.addView(ChainView.viewId);
		
		// Bottom folder
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.70, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		
		IFolderLayout bottom2 = layout.createFolder("bottom2", IPageLayout.BOTTOM, (float) 0.70, editorArea);
		bottom2.addView(ChangesView.viewId);
	}
}
