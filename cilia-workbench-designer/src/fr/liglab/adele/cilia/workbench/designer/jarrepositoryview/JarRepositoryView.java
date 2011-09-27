/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.EditorsUI;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Bundle;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.RepositoryView;

/**
 * The Class RepositoryView.
 */
public class JarRepositoryView extends RepositoryView {

	/** The Constant viewId. */
	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.jarrepositoryview";

	/** The model. */
	private Bundle[] model = new Bundle[0];

	/**
	 * Instantiates a new repository view.
	 */
	public JarRepositoryView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		viewer.setLabelProvider(new MetadataLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openMetadataInEditor();
			}
		});
	}

	/**
	 * Refresh viewer.
	 */
	public void refresh() {
		super.refresh();

		File dir = new File(getRepositoryDirectory());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		List<Bundle> bundles = new ArrayList<Bundle>();
		if (list != null) {
			for (File jar : list) {
				try {
					String path = jar.getPath();
					bundles.add(new Bundle(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		model = bundles.toArray(new Bundle[0]);
		viewer.setContentProvider(new MetadataContentProvider(model));
		viewer.setInput(model);
		viewer.refresh();
	}

	/**
	 * Opens an editor with the content of the related metadata. Does'nt open an
	 * editor twice. Only bring to top the second time.
	 */
	private void openMetadataInEditor() {
		ISelectionService selServ = getSite().getWorkbenchWindow().getSelectionService();
		ISelection sel = selServ.getSelection();
		if (sel != null && sel instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) sel;
			Object element = ts.getFirstElement();
			if (element instanceof Bundle) {
				Bundle bundle = (Bundle) element;
				String bundleName = bundle.getBundleName();

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

				IStorage storage = new StreamFromFileStorage(bundleName);
				IStorageEditorInput input = new StringInput(storage);
				try {
					page.openEditor(input, EditorsUI.DEFAULT_TEXT_EDITOR_ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	@Override
	protected String getRepositoryPropertyPath() {
		return CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;
	}
}
