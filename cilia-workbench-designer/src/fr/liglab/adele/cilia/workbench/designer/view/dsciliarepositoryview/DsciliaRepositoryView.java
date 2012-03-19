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
package fr.liglab.adele.cilia.workbench.designer.view.dsciliarepositoryview;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.RepositoryView;

/**
 * DsciliaRepositoryView.
 */
public class DsciliaRepositoryView extends RepositoryView<DsciliaFile> {

	/** The viewId. */
	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.view.dsciliarepositoryview";

	public DsciliaRepositoryView() {
		super(DsciliaRepoService.getInstance());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.liglab.adele.cilia.workbench.designer.repositoryview.RepositoryView
	 * #createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setLabelProvider(new DsciliaLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openEditor(event);
			}
		});

		// Linking already opened editors for getting save notifications
		IEditorReference[] ref = getViewSite().getPage().getEditorReferences();
		for (IEditorReference editor : ref) {
			if (editor.getTitle().toLowerCase().endsWith(".dscilia")) {
				addEditorSavedListener(editor.getPart(true));
			}
		}
	}

	/**
	 * Called when DScilia repository changes. Refresh view if needed.
	 * 
	 * @param changes
	 *            the changes
	 */
	@Override
	public void repositoryContentUpdated(Changeset[] changes) {
		for (Changeset change : changes) {
			Object object = change.getObject();
			Operation operation = change.getOperation();
			if (operation != Operation.UPDATE) {
				if (object instanceof DsciliaFile || object instanceof Chain) {
					refresh();
					return;
				}
			} else {
				if (object instanceof DsciliaFile) {
					refresh();
					return;
				}
			}
		}
	}

	/**
	 * Adds a listener ton an editor to be notify as soon as he saves.
	 * 
	 * @param editor
	 *            the editor
	 */
	private void addEditorSavedListener(IWorkbenchPart editor) {
		if (editor != null) {
			editor.addPropertyListener(new IPropertyListener() {
				@Override
				public void propertyChanged(Object source, int propId) {
					if (propId == IEditorPart.PROP_DIRTY && source instanceof EditorPart) {
						EditorPart editor = (EditorPart) source;
						if (editor.isDirty() == false)
							repoService.updateModel();
					}
				}
			});
		}
	}

	/**
	 * Opens an editor.
	 * 
	 * @param event
	 *            the event
	 */
	private void openEditor(DoubleClickEvent event) {
		Object element = getFirstSelectedElement();

		if (element != null && element instanceof DsciliaFile) {
			DsciliaFile repoElement = (DsciliaFile) element;
			if (repoElement.getModel() != null) {
				IFileStore fileStore;
				try {
					fileStore = EFS.getLocalFileSystem().getStore(new URI(repoElement.getFilePath()));
					IWorkbenchPage page = getViewSite().getPage();
					IEditorPart editor = IDE.openEditorOnFileStore(page, fileStore);
					addEditorSavedListener(editor);
				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
}