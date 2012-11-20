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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.dsciliachain;

import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionListener;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionService;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview.DSCiliaRepositoryView;

/**
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaChainView extends GraphView implements IRepoServiceListener, SelectionListener {
	public static final String viewId = "fr.liglab.adele.cilia.workbench.designer.view.dsciliachainview";

	private IBaseLabelProvider labelProvider = new DSCiliaChainLabelProvider();
	private IContentProvider contentProvider = new DSCiliaChainContentProvider();

	private DSCiliaChain model = null;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);

		DSCiliaRepoService.getInstance().registerListener(this);
		SelectionService.getInstance().addSelectionListener(DSCiliaRepositoryView.VIEW_ID, this);

		updateConfigAndModel(null);
	}

	public DSCiliaChain getModel() {
		return model;
	}

	private void updateConfigAndModel(DSCiliaChain chain) {
		this.model = chain;
		if (viewer.getLabelProvider() != labelProvider)
			viewer.setLabelProvider(labelProvider);

		if (viewer.getContentProvider() != contentProvider)
			viewer.setContentProvider(contentProvider);

		viewer.setInput(chain);

		if (model == null)
			setPartName("DSCilia Chain Viewer");
		else
			setPartName("DSCilia Chain: " + model.getName());

		ToggleSourceProvider.setToggleVariable(DSCiliaChainToolbarEnabler.VARIABLE_NAME, model != null);

		viewer.refresh();
	}

	@Override
	public void selectionChanged(String partId, ISelection selection) {
		if (selection != null && selection instanceof TreeSelection) {
			TreeSelection sel = (TreeSelection) selection;
			if (sel.getFirstElement() != null && sel.getFirstElement() instanceof DSCiliaChain)
				updateConfigAndModel((DSCiliaChain) sel.getFirstElement());
		}
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		// if model = null, no need to check anything...
		if (model != null) {
			boolean needUpdate = false;
			for (Changeset change : changes) {

				// Repository removed
				if (change.getObject() instanceof DSCiliaFile && change.getOperation() == Operation.REMOVE) {
					DSCiliaModel removedModel = ((DSCiliaFile) (change.getObject())).getModel();
					if (removedModel != null) {
						for (DSCiliaChain removedChain : removedModel.getChains()) {
							if (removedChain == model) {
								updateConfigAndModel(null);
								return;
							}
						}
					}
				}

				// Chain removed
				if (change.getObject() instanceof DSCiliaChain && change.getOperation() == Operation.REMOVE) {
					if (model == change.getObject()) { // pointer equality
						updateConfigAndModel(null);
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
				updateConfigAndModel(model);
			}
		}
	}

	@Override
	protected void onDoubleClick(Shell parentShell, Object element) {
		ViewUtil.notYetImplementedHandler(parentShell);
	}
}
