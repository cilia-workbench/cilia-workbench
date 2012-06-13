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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.common.ui.view.GraphView;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.GraphContentProvider;

/**
 * The Class ChainView.
 * 
 * @author Etienne Gandrille
 */
public class ChainView extends GraphView {

	/** The View ID. */
	public static final String viewId = "fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview";

	public ChainView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);

		// Registers the instance in the selection service
		ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
		s.addSelectionListener(TopologyView.viewId, this);

		viewer.setContentProvider(new GraphContentProvider());
		viewer.setLabelProvider(new NodeElement(null));
		viewer.setInput(new Object[0]);
	}

	/**
	 * Sets the model.
	 * 
	 * @param chain
	 *            the new model
	 */
	private void setModel(ChainReadOnly chain) {
		List<Object> list = new ArrayList<Object>();

		for (Object o : chain.getAdapters()) {
			AdapterReadOnly adapt = (AdapterReadOnly) o;
			list.add(new NodeElement(adapt));
		}

		for (Object o : chain.getMediators()) {
			MediatorReadOnly mediator = (MediatorReadOnly) o;
			list.add(new NodeElement(mediator));
		}

		viewer.setInput(list);
		viewer.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object element = treeSelection.getFirstElement();

			if (element instanceof ChainReadOnly) {
				ChainReadOnly chain = (ChainReadOnly) element;
				setModel(chain);
			}

			if (element instanceof MediatorReadOnly) {
				MediatorReadOnly mediator = (MediatorReadOnly) element;

				IViewReference[] views = getSite().getPage().getViewReferences();
				for (IViewReference view : views) {
					if (view.getId().equals(TopologyView.viewId)) {
						TopologyView tv = (TopologyView) view.getView(true);
						CiliaContextReadOnly ccro = tv.getModel().getCiliaContextRO();
						ChainReadOnly parent = CiliaUtil.getMediatorParent(ccro, mediator);
						if (parent != null)
							setModel(parent);
					}
				}
			}
		}
	}
}
