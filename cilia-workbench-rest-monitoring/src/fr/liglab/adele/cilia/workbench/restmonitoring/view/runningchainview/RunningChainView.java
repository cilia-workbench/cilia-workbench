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
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
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

	private final IBaseLabelProvider defaultLabelProvider = new PlatformChainLabelProvider();

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, VIEW_ID);

		PlatformRepoService.getInstance().registerListener(this);
		SelectionService.getInstance().addSelectionListener(PlatformView.VIEW_ID, this);
		SelectionService.getInstance().addSelectionListener(AbstractChainView.VIEW_ID, this);
		SelectionService.getInstance().addSelectionListener(RunningChainView.VIEW_ID, this);

		updateConfigAndModel(null);
	}

	private void setLabelProvider(IBaseLabelProvider labelProvider) {
		if (viewer.getLabelProvider() != labelProvider)
			viewer.setLabelProvider(labelProvider);
	}

	@Override
	public void dispose() {
		super.dispose();
		PlatformRepoService.getInstance().unregisterListener(this);
		SelectionService.getInstance().removeSelectionListener(PlatformView.VIEW_ID, this);
		SelectionService.getInstance().removeSelectionListener(AbstractChainView.VIEW_ID, this);
		SelectionService.getInstance().removeSelectionListener(RunningChainView.VIEW_ID, this);
	}

	public PlatformChain getModel() {
		return model;
	}

	private void updateConfigAndModel(PlatformChain chain) {
		model = chain;
		setLabelProvider(defaultLabelProvider);
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

			if (selection instanceof TreeSelection) {
				TreeSelection sel = (TreeSelection) selection;
				if (sel.getFirstElement() != null && sel.getFirstElement() instanceof PlatformChain)
					updateConfigAndModel((PlatformChain) sel.getFirstElement());
			}

			// selection in the reference architecture view
			if (partId.equals(AbstractChainView.VIEW_ID)) {
				StructuredSelection sel = (StructuredSelection) selection;
				Object element = sel.getFirstElement();
				if (element instanceof ComponentRef) {
					ComponentRef component = (ComponentRef) element;
					String componentId = component.getId();
					NameNamespaceID chainId = ((AbstractChain) component.getChain()).getId();

					if (model != null) {
						if (model.getRefArchitectureID() != null && model.getRefArchitectureID().equals(chainId)) {
							setLabelProvider(new CrossSelectionChainLabelProvider(componentId));
							viewer.refresh();
						}
					}
				} else if (element == null) {
					setLabelProvider(defaultLabelProvider);
					viewer.refresh();
				}
			}

			// when a new selection is made in this view, reset the label
			// provider to default !
			if (partId.equals(VIEW_ID)) {
				setLabelProvider(defaultLabelProvider);
				viewer.refresh();
			}
		}
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		// if model = null, no need to check anything...
		if (model != null) {
			boolean needUpdate = false;
			for (Changeset change : changes) {

				// Platform removed
				if (change.getObject() instanceof PlatformFile && change.getOperation() == Operation.REMOVE) {
					PlatformModel removedModel = ((PlatformFile) (change.getObject())).getModel();
					if (removedModel != null) {
						// This code is different from DSCiliaChainView because
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

				// Chain content modified
				if (change.getPath().contains(model) && model != change.getObject()) {
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
		try {
			new PropertiesDialog(parentShell, model, (ComponentRef) element).open();
		} catch (CiliaException e) {
			MessageDialog.openError(parentShell, "Can't obtain information", e.toString());
		}
	}
}
