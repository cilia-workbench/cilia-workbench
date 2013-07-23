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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionListener;
import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionService;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.common.service.chain.ChainContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.NodeSelector;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.StrongHighlightGraphLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain.AbstractChainGraphTextLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain.AbstractChainView;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview.PlatformView;
import fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview.dialog.PropertiesDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class RunningChainView extends GraphView implements IRepoServiceListener, SelectionListener {

	public static final String VIEW_ID = "fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview";

	private PlatformChain model = null;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, VIEW_ID);

		PlatformRepoService.getInstance().registerListener(this);
		SelectionService.getInstance().addSelectionListener(PlatformView.VIEW_ID, this);
		SelectionService.getInstance().addSelectionListener(AbstractChainView.VIEW_ID, this);
		SelectionService.getInstance().addSelectionListener(VIEW_ID, this);
		AbstractCompositionsRepoService.getInstance().registerListener(this);

		updateConfigAndModel(null);
	}

	@Override
	public void dispose() {
		super.dispose();
		PlatformRepoService.getInstance().unregisterListener(this);
		SelectionService.getInstance().removeSelectionListener(PlatformView.VIEW_ID, this);
		SelectionService.getInstance().removeSelectionListener(AbstractChainView.VIEW_ID, this);
		SelectionService.getInstance().removeSelectionListener(VIEW_ID, this);
		AbstractCompositionsRepoService.getInstance().unregisterListener(this);
	}

	public PlatformChain getModel() {
		return model;
	}

	private void updateConfigAndModel(PlatformChain chain) {
		model = chain;
		setLabelProvider(getRunningChainViewDefaultLP());
		viewer.setContentProvider(new PlatformChainContentProvider(chain));
		viewer.setInput(chain);
		updatePartName();
		viewer.refresh();
	}

	private void updatePartName() {
		if (model == null)
			setPartName("Platform Chain Viewer");
		else
			setPartName("Platform Chain: " + model.getName());
		ToggleSourceProvider.setToggleVariable(RunningChainToolbarEnabler.VARIABLE_NAME, model != null);
	}

	@Override
	public void selectionChanged(String partId, ISelection selection) {
		if (selection != null) {

			// selection in the platform view tree
			if (partId.equals(PlatformView.VIEW_ID) && selection instanceof TreeSelection) {
				TreeSelection sel = (TreeSelection) selection;
				if (sel.getFirstElement() != null && sel.getFirstElement() instanceof PlatformChain)
					updateConfigAndModel((PlatformChain) sel.getFirstElement());
			}

			// selection in the reference architecture view graph
			if (partId.equals(AbstractChainView.VIEW_ID)) {
				boolean linkExists = false;

				// check if link exists between what is displayed in Reference
				// archi view and running view
				ComponentRef component = getComponentRefFromSelection(selection);
				if (component != null && model != null) {
					NameNamespaceID chainId = ((AbstractChain) component.getChain()).getId();
					if (chainId != null && model.getRefArchitectureID() != null && model.getRefArchitectureID().equals(chainId))
						linkExists = true;
				}

				// updates running chain view
				if (linkExists) {
					setLabelProvider(getRunningChainViewCrossLP(component.getId()));
					refresh();
				} else {
					setLabelProvider(getRunningChainViewDefaultLP());
					refresh();
				}

				// updates abstract chain view
				AbstractChainView abstractChainView = (AbstractChainView) ViewUtil.findViewWithId(this, AbstractChainView.VIEW_ID);
				if (abstractChainView != null) {
					abstractChainView.setLabelProvider(getAbstractChainViewDefaultLP());
					abstractChainView.refresh();
				}
			}

			// when a new selection is performed in this view
			if (partId.equals(VIEW_ID)) {
				boolean linkExists = false;
				AbstractChainView abstractChainView = (AbstractChainView) ViewUtil.findViewWithId(this, AbstractChainView.VIEW_ID);

				// check if link exists between what is displayed in Reference
				// archi view and running view
				ComponentRef selectedComponent = getComponentRefFromSelection(selection);
				if (selectedComponent != null && model != null && model.getRefArchitectureID() != null) {
					if (abstractChainView != null && abstractChainView.getModel() != null) {
						if (model.getRefArchitectureID().equals(abstractChainView.getModel().getId()))
							linkExists = true;
					}
				}

				// updates abstract chain view
				if (linkExists) {
					abstractChainView.setLabelProvider(getAbstractChainViewCrossLP(selectedComponent.getId()));
					abstractChainView.refresh();
				} else {
					abstractChainView.setLabelProvider(getAbstractChainViewDefaultLP());
					abstractChainView.refresh();
				}

				// updates running chain view
				setLabelProvider(getRunningChainViewDefaultLP());
				refresh();
			}
		}
	}

	private static ComponentRef getComponentRefFromSelection(ISelection selection) {
		if (selection != null && selection instanceof StructuredSelection) {
			StructuredSelection sel = (StructuredSelection) selection;
			Object element = sel.getFirstElement();
			if (element != null && element instanceof ComponentRef) {
				return (ComponentRef) element;
			}
		}
		return null;
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		// if model = null, no need to check anything...
		if (model != null) {
			if (abstractRepoService instanceof PlatformRepoService) {

				for (Changeset change : changes) {

					// Platform removed
					if (change.getObject() instanceof PlatformFile && change.getOperation() == Operation.REMOVE) {
						PlatformModel removedModel = ((PlatformFile) (change.getObject())).getModel();
						if (removedModel != null) {
							// This code is different from DSCiliaChainView
							// because
							// two platforms can host a
							// chain with the same name !
							if (model.getPlatform() == removedModel) {
								updateConfigAndModel(null);
								return;
							}
						}
					}

					// Chain removed
					if (change.getObject() instanceof PlatformChain && change.getOperation() == Operation.REMOVE) {
						if (model == change.getObject()) { // pointer equality
							updateConfigAndModel(null);
							return;
						}
					}
				}

				updateConfigAndModel(model);
			}

			else if (abstractRepoService instanceof AbstractCompositionsRepoService) {
				updateConfigAndModel(model);
			}
		}
	}

	@Override
	protected void onDoubleClick(Shell parentShell, Object element) {
		try {
			new PropertiesDialog(parentShell, model, (ComponentRef) element).open();
		} catch (CiliaException e) {
			MessageDialog.openError(parentShell, "Can't obtain information", e.toString());
		}
	}

	// =========================
	// LABEL PROVIDERS FACTORIES
	// =========================

	private IBaseLabelProvider getRunningChainViewDefaultLP() {
		ChainContentProvider cp = PlatformRepoService.getInstance().getContentProvider();
		return new GraphLabelProvider(cp);
	}

	private IBaseLabelProvider getRunningChainViewCrossLP(String componentId) {
		ChainContentProvider cp = PlatformRepoService.getInstance().getContentProvider();
		NodeSelector checker = new StrongHighlightNodeSelectorForRunningGraph(componentId);
		return new StrongHighlightGraphLabelProvider(cp, checker);
	}

	private IBaseLabelProvider getAbstractChainViewDefaultLP() {
		return AbstractChainView.defaultLabelProvider;
	}

	private IBaseLabelProvider getAbstractChainViewCrossLP(String componentId) {
		ChainContentProvider cp = AbstractCompositionsRepoService.getInstance().getContentProvider();
		AbstractChainGraphTextLabelProvider tlp = new AbstractChainGraphTextLabelProvider();
		NodeSelector checker = new StrongHighlightNodeSelectorForAbstractGraph(componentId);
		return new StrongHighlightGraphLabelProvider(cp, tlp, checker);
	}
}
