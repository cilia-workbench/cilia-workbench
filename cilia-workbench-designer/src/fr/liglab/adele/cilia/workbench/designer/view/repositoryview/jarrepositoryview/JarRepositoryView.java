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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.jarrepositoryview;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;

import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryView;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class JarRepositoryView extends RepositoryView<CiliaJarFile, CiliaJarModel> {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.designer.view.jarrepositoryview";

	public JarRepositoryView() {
		super(VIEW_ID, CiliaJarRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setLabelProvider(new JarMetadataLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openMetadataInEditor();
			}
		});
	}

	/**
	 * Opens an editor with the content of the related metadata. Does'nt open an
	 * editor twice. Only bring to top the second time.
	 */
	private void openMetadataInEditor() {

		Object element = getFirstSelectedElement();
		if (element != null && element instanceof CiliaJarFile) {
			CiliaJarFile bundle = (CiliaJarFile) element;

			IWorkbenchPage page = getViewSite().getPage();

			IEditorReference[] refs = page.getEditorReferences();
			for (IEditorReference ref : refs) {
				try {
					IEditorInput input = ref.getEditorInput();
					if (input instanceof StringInput) {
						StringInput si = (StringInput) input;
						String viewName = si.getName();
						if (bundle.toString().equals(viewName)) {
							IEditorPart editor = page.findEditor(input);
							page.bringToTop(editor);
							return;
						}
					}
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}

			IStorage storage = new StreamFromFileStorage(bundle.getResource());
			IStorageEditorInput input = new StringInput(storage);
			try {
				IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
				IEditorDescriptor ed = registry.getDefaultEditor(".xml");
				page.openEditor(input, ed != null ? ed.getId() : EditorsUI.DEFAULT_TEXT_EDITOR_ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String getPreferencePageID() {
		return CiliaDesignerPreferencePage.PAGE_ID;
	}
}
