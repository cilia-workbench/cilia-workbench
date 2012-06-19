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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorInstanceRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.view.abstractcompositionsview.AbstractCompositionsView;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.ChainDesignerConfiguration;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.ChainDesignerView;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainConfiguration extends ChainDesignerConfiguration<AbstractCompositionsRepoService, Chain> {

	public AbstractChainConfiguration(ChainDesignerView parent) {
		super(parent, AbstractCompositionsRepoService.getInstance(), AbstractCompositionsView.VIEW_ID,
				new AbstractChainContentProvider(), new AbstractChainLabelProvider());
	}

	public Object createAdapterHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			NewAdapterDialog window = new NewAdapterDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				String id = window.getText();
				IGenericAdapter adapter = (IGenericAdapter) window.getValue();
				try {
					AbstractCompositionsRepoService.getInstance().createAdapter(model, id, adapter);
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public Object deleteAdapterHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			DeleteAdapterDialog window = new DeleteAdapterDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				Object[] objects = window.getResult();
				if (objects.length != 0) {
					AdapterRef component = (AdapterRef) objects[0];
					try {
						AbstractCompositionsRepoService.getInstance().deleteComponent(model, component);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	public Object createMediatorHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			NewMediatorDialog window = new NewMediatorDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				String id = window.getText();
				IGenericMediator mediator = (IGenericMediator) window.getValue();
				try {
					AbstractCompositionsRepoService.getInstance().createMediator(model, id, mediator);
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public Object deleteMediatorHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			DeleteMediatorDialog window = new DeleteMediatorDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				Object[] objects = window.getResult();
				if (objects.length != 0) {
					MediatorRef component = (MediatorRef) objects[0];
					try {
						AbstractCompositionsRepoService.getInstance().deleteComponent(model, component);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	public Object createBindingHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			NewBindingDialog window = new NewBindingDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				String srcElem = window.getSrcElem();
				String dstElem = window.getDstElem();
				String srcPort = window.getSrcPort();
				String dstPort = window.getDstPort();
				Cardinality srcCard = window.getSrcCardinality();
				Cardinality dstCard = window.getDstCardinality();

				try {
					AbstractCompositionsRepoService.getInstance().createBinding(model, srcElem, srcPort, dstElem,
							dstPort, srcCard, dstCard);
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public Object deleteBindingHandler(ExecutionEvent event) throws ExecutionException {
		if (model != null) {
			DeleteBindingDialog window = new DeleteBindingDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				Object[] objects = window.getResult();
				if (objects.length != 0) {
					Binding binding = (Binding) objects[0];
					try {
						AbstractCompositionsRepoService.getInstance().deleteBinding(model, binding);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	@Override
	public void doubleClickHandler(Shell shell, Object element) {
		if (element instanceof MediatorRef && model != null) {
			MediatorRef mediator = (MediatorRef) element;

			UpdateMediatorRefDialog dialog;
			if (mediator instanceof MediatorSpecRef)
				dialog = new UpdateMediatorSpecRefDialog(shell, (MediatorSpecRef) mediator);
			else
				dialog = new UpdateMediatorInstanceRefDialog(shell, (MediatorInstanceRef) mediator);

			if (dialog.open() == Window.OK) {

				// Constraints
				if (mediator instanceof MediatorSpecRef) {
					try {
						AbstractCompositionsRepoService.getInstance().updateProperties(model,
								(MediatorSpecRef) mediator, ((UpdateMediatorSpecRefDialog) dialog).getConstraints());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Parameters
				Map<String, String> sParam = dialog.getSchedulerParameters();
				Map<String, String> pParam = dialog.getProcessorParameters();
				Map<String, String> dParam = dialog.getDispatcherParameters();
				try {
					AbstractCompositionsRepoService.getInstance().updateParameters(model, mediator, sParam, pParam,
							dParam);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
}
