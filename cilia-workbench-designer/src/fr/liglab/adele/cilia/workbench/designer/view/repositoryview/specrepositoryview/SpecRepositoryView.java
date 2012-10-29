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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.specrepositoryview;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryView;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecModel;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class SpecRepositoryView extends RepositoryView<SpecFile, SpecModel> {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.designer.view.specrepositoryview";

	public SpecRepositoryView() {
		super(VIEW_ID, SpecRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object element = getFirstSelectedElement();

				if (element != null) {
					if (element instanceof SpecFile)
						openFileEditor(event);
					else if (element instanceof MediatorSpec)
						editMediatorSpec(event, (MediatorSpec) element);
				}
			}
		});

		// Linking already opened editors for getting save notifications
		for (IEditorReference editor : getRelevantFileStoreEditors(".xml"))
			addEditorSavedListener(editor.getPart(true));

		viewer.setLabelProvider(new SpecLabelProvider());
		viewer.setAutoExpandLevel(3);
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		if (changes != null && changes.size() != 0) {
			refresh();
			return;
		}

		// updates labels and icons
		refreshMessageArea();
		viewer.refresh(true);
	}

	private void editMediatorSpec(DoubleClickEvent event, MediatorSpec element) {

		UpdateMediatorSpecDialog dialog = new UpdateMediatorSpecDialog(ViewUtil.getShell(event), element);

		if (dialog.open() == Window.OK) {
			SpecRepoService.getInstance().updateMediatorSpec(element, dialog.getInPorts(), dialog.getOutPorts(), dialog.getMediatorProperties(),
					dialog.getSchedulerParam(), dialog.getProcessorParam(), dialog.getDispatcherParam());
		}
	}
}
