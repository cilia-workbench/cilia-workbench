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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryView;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformView extends RepositoryView<PlatformFile, PlatformModel> implements ISelectionChangedListener {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview";

	public PlatformView() {
		super(PlatformRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.addSelectionChangedListener(this);
		viewer.setLabelProvider(new PlatformLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openFileEditor(event);
			}
		});

		// Linking already opened editors for getting save notifications
		for (IEditorReference editor : getRelevantFileStoreEditors(PlatformRepoService.ext))
			addEditorSavedListener(editor.getPart(true));
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		for (Changeset change : changes) {
			Object object = change.getObject();
			if (object instanceof PlatformFile || object instanceof PlatformChain) {
				refresh();
				return;
			}
		}

		// updates labels and icons
		refreshMessageArea();
		viewer.refresh(true);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO on regarde ce qui est sélectionné...
		// si c'est une chaine, il faut mettre à jour le modèle par une requête
		// rest.

		if (event != null && event.getSelection() != null && event.getSelection() instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) event.getSelection();
			Object elm = ss.getFirstElement();
			if (elm != null)
				System.out.println(elm);
		}

		System.out.println(event.getSelection());
	}
}
