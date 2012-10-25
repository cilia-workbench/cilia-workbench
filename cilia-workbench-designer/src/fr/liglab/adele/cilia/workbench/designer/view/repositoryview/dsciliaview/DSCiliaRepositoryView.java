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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryView;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.ConcreteChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaRepositoryView extends RepositoryView<DSCiliaFile, DSCiliaModel> {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.designer.view.dsciliaview";

	public DSCiliaRepositoryView() {
		super(VIEW_ID, DSCiliaRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setLabelProvider(new DSCiliaLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openFileEditor(event);
			}
		});

		// Linking already opened editors for getting save notifications
		for (IEditorReference editor : getRelevantFileStoreEditors(DSCiliaRepoService.ext))
			addEditorSavedListener(editor.getPart(true));
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		for (Changeset change : changes) {
			Object object = change.getObject();
			Operation operation = change.getOperation();
			if (operation != Operation.UPDATE) {
				if (object instanceof DSCiliaFile || object instanceof ConcreteChain) {
					refresh();
					return;
				}
			} else {
				if (object instanceof DSCiliaFile) {
					refresh();
					return;
				}
			}
		}

		// updates labels and icons
		refreshMessageArea();
		viewer.refresh(true);
	}
}