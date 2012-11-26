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
package fr.liglab.adele.cilia.workbench.restmonitoring.misc.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview.CiliaErrorView;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain.AbstractChainView;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.dsciliachain.DSCiliaChainView;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.abstractcompositionsview.AbstractCompositionsView;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview.DSCiliaRepositoryView;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.jarrepositoryview.JarRepositoryView;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.specrepositoryview.SpecRepositoryView;
import fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview.PlatformView;
import fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview.RunningChainView;

/**
 * The REST Monitoring perspective.
 * 
 * @author Etienne Gandrille
 */
public class RestMonitoringPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();

		// Left folder
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea);
		left.addView(SpecRepositoryView.VIEW_ID);
		left.addView(JarRepositoryView.VIEW_ID);

		// Bottom folder
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.80, editorArea);
		bottom.addView(CiliaErrorView.VIEW_ID);
		bottom.addView(IPageLayout.ID_PROP_SHEET);

		// Top Right part
		layout.addView(AbstractCompositionsView.VIEW_ID, IPageLayout.TOP, (float) 0.25, editorArea);
		layout.addView(AbstractChainView.viewId, IPageLayout.RIGHT, (float) 0.50, AbstractCompositionsView.VIEW_ID);

		layout.addView(DSCiliaRepositoryView.VIEW_ID, IPageLayout.TOP, (float) 0.33, editorArea);
		layout.addView(DSCiliaChainView.VIEW_ID, IPageLayout.RIGHT, (float) 0.50, DSCiliaRepositoryView.VIEW_ID);

		layout.addView(PlatformView.VIEW_ID, IPageLayout.TOP, (float) 0.50, editorArea);
		layout.addView(RunningChainView.VIEW_ID, IPageLayout.RIGHT, (float) 0.50, PlatformView.VIEW_ID);
	}
}
