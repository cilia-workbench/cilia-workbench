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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.common.parser.ChainFile;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ChainModel;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionListener;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionService;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.common.service.chain.ChainContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.MediatorImplemRef;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.UpdateMediatorInstanceRefDialog;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.UpdateMediatorRefDialog;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.UpdateMediatorSpecRefDialog;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.abstractcompositionsview.AbstractCompositionsView;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainView extends GraphView implements IRepoServiceListener, SelectionListener {

	public static final String VIEW_ID = "fr.liglab.adele.cilia.workbench.designer.view.abstractchainview";

	public static final IBaseLabelProvider defaultLabelProvider = new GraphLabelProvider(
			(ChainContentProvider<? extends ChainFile<? extends ChainModel<? extends Chain>, ? extends Chain>, ? extends ChainModel<? extends Chain>, ? extends Chain>) AbstractCompositionsRepoService
					.getInstance().getContentProvider(), new AbstractChainGraphTextLabelProvider());
	private IContentProvider contentProvider = new AbstractChainContentProvider();

	private AbstractChain model = null;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, VIEW_ID);

		AbstractCompositionsRepoService.getInstance().registerListener(this);
		SelectionService.getInstance().addSelectionListener(AbstractCompositionsView.VIEW_ID, this);

		updateConfigAndModel(null);
	}

	@Override
	public void dispose() {
		super.dispose();
		AbstractCompositionsRepoService.getInstance().unregisterListener(this);
		SelectionService.getInstance().removeSelectionListener(AbstractCompositionsView.VIEW_ID, this);
	}

	public AbstractChain getModel() {
		return model;
	}

	private void updateConfigAndModel(AbstractChain chain) {
		this.model = chain;
		if (viewer.getLabelProvider() != defaultLabelProvider)
			viewer.setLabelProvider(defaultLabelProvider);

		if (viewer.getContentProvider() != contentProvider)
			viewer.setContentProvider(contentProvider);

		viewer.setInput(chain);

		if (model == null)
			setPartName("Abstract Chain Viewer");
		else
			setPartName("Abstract Chain: " + model.getName());

		ToggleSourceProvider.setToggleVariable(AbstractChainToolbarEnabler.VARIABLE_NAME, model != null);

		viewer.refresh();
	}

	@Override
	public void selectionChanged(String partId, ISelection selection) {
		if (selection != null) {
			if (partId.equals(AbstractCompositionsView.VIEW_ID) && selection instanceof TreeSelection) {
				TreeSelection sel = (TreeSelection) selection;
				if (sel.getFirstElement() != null && sel.getFirstElement() instanceof AbstractChain)
					updateConfigAndModel((AbstractChain) sel.getFirstElement());
			}
		}
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		// if model = null, no need to check anything...
		if (model != null) {
			AbstractCompositionsRepoService srv = AbstractCompositionsRepoService.getInstance();

			boolean needUpdate = false;
			for (Changeset change : changes) {

				// Repository removed
				if (change.getObject() instanceof AbstractCompositionFile && change.getOperation() == Operation.REMOVE) {
					AbstractCompositionFile curRepo = srv.getRepoElement(model);
					if (curRepo == change.getObject()) { // pointer equality
						updateConfigAndModel(null);
						return;
					}
				}

				// Chain removed
				if (change.getObject() instanceof AbstractChain && change.getOperation() == Operation.REMOVE) {
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
		System.out.println("double click !");

		if (element instanceof MediatorRef && model != null) {
			MediatorRef mediator = (MediatorRef) element;

			UpdateMediatorRefDialog dialog;
			if (mediator instanceof MediatorSpecRef)
				dialog = new UpdateMediatorSpecRefDialog(parentShell, (MediatorSpecRef) mediator);
			else
				dialog = new UpdateMediatorInstanceRefDialog(parentShell, (MediatorImplemRef) mediator);

			if (dialog.open() == Window.OK) {

				// Constraints
				if (mediator instanceof MediatorSpecRef) {
					try {
						AbstractCompositionsRepoService.getInstance().updateProperties(model, (MediatorSpecRef) mediator,
								((UpdateMediatorSpecRefDialog) dialog).getConstraints());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Parameters
				Map<String, String> sParam = dialog.getSchedulerParameters();
				Map<String, String> pParam = dialog.getProcessorParameters();
				Map<String, String> dParam = dialog.getDispatcherParameters();
				try {
					AbstractCompositionsRepoService.getInstance().updateParameters(model, mediator, sParam, pParam, dParam);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
