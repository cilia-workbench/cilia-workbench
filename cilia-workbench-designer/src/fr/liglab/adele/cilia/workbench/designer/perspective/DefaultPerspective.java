package fr.liglab.adele.cilia.workbench.designer.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.DsciliaRepositoryView;
import fr.liglab.adele.cilia.workbench.designer.jarrepositoryview.JarRepositoryView;

public class DefaultPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		// Editors are placed for free.
		String editorArea = layout.getEditorArea();

		// Left folder
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.20, editorArea);
		left.addView(JarRepositoryView.viewId);
		left.addView(DsciliaRepositoryView.viewId);
		
		// Bottom folder
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.70, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);

	}
}
