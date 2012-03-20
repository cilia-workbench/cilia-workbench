/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.monitoring.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.liglab.adele.cilia.workbench.monitoring.changesview.ChangesView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview.ChainView;

/**
 *
 * @author Etienne Gandrille
 */
public class DefaultPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Editors are placed for free.
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// Left folder
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.20, editorArea);
		left.addView(TopologyView.viewId);
				
		
		// Top folder
		IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, (float) 0.30, editorArea);
		top.addView(ChainView.viewId);
		
		// Bottom folder
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.50, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		
		IFolderLayout bottom2 = layout.createFolder("bottom2", IPageLayout.BOTTOM, (float) 0.25, editorArea);
		bottom2.addView(ChangesView.viewId);
	}
}
