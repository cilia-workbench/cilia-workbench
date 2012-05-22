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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.workbench.common.sourceprovider.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.common.view.GraphView;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.designer.view.abstractcompositionsview.AbstractCompositionsView;

/**
 * The chain designer view.
 * 
 * @author Etienne Gandrille
 */
public class ChainDesignerView extends GraphView implements IRepoServiceListener {

	/** The View ID. */
	public static final String viewId = "fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview";

	private ChainGraphContentProvider contentProvider = new ChainGraphContentProvider();

	private Chain model;

	private final String DEFAULT_PART_NAME = "Chain Viewer";

	public ChainDesignerView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);

		// Registers the instance in the selection service
		ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
		s.addSelectionListener(AbstractCompositionsView.VIEW_ID, this);

		setPartName(DEFAULT_PART_NAME);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new GraphLabelProvider());
		viewer.setInput(new Object[0]);
		AbstractCompositionsRepoService.getInstance().registerListener(this);
	}

	private void setModel(Chain chain) {
		contentProvider.setModel(chain);
		if (chain != null) {
			viewer.setInput(chain.getComponents());
			setPartName(chain.getName());
		} else {
			viewer.setInput(new Object[0]);
			setPartName(DEFAULT_PART_NAME);
		}
		ToggleSourceProvider.setToggleVariable(ToolbarEnabler.VARIABLE_NAME, chain != null);
		viewer.refresh();
		this.model = chain;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object element = treeSelection.getFirstElement();

			if (element instanceof Chain) {
				Chain chain = (Chain) element;
				setModel(chain);
			}
		}
	}

	@Override
	public void repositoryContentUpdated(List<Changeset> changes) {
		// if model = null, no need to check anything...
		if (model != null) {
			AbstractCompositionsRepoService srv = AbstractCompositionsRepoService.getInstance();

			boolean needUpdate = false;
			for (Changeset change : changes) {

				// Repository removed
				if (change.getObject() instanceof AbstractCompositionFile && change.getOperation() == Operation.REMOVE) {
					AbstractCompositionFile curRepo = srv.getRepoElement(model);
					if (curRepo == change.getObject()) { // pointer equality
						setModel(null);
						return;
					}
				}

				// Chain removed
				if (change.getObject() instanceof Chain && change.getOperation() == Operation.REMOVE) {
					if (model == change.getObject()) { // pointer equality
						setModel(null);
						return;
					}
				}

				// Chain content modified
				if (change.getPath().contains(model) && model != change.getObject()) {
					if (change.getOperation() == Operation.REMOVE || change.getOperation() == Operation.ADD)
						needUpdate = true;
				}
			}

			if (needUpdate == true) {
				// viewer.refresh();
				setModel(model);
			}
		}
	}

	public Chain getModel() {
		return model;
	}
}
