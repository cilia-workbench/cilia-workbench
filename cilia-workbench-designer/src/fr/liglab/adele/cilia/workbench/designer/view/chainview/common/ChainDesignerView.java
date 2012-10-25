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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain.AbstractChainConfiguration;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.dscilia.DSCiliaConfiguration;

/**
 * 
 * @author Etienne Gandrille
 */
public class ChainDesignerView extends GraphView implements IRepoServiceListener, ISelectionListener {

	public static final String viewId = "fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview";

	private long lastEvent = 0;

	private Map<String, ChainDesignerConfiguration<? extends ChainRepoService<?, ?, ?>, ? extends GraphDrawable>> configs = new HashMap<String, ChainDesignerConfiguration<? extends ChainRepoService<?, ?, ?>, ? extends GraphDrawable>>();
	String currentConfig = null;

	public ChainDesignerView() {
	}

	public ChainDesignerConfiguration<? extends ChainRepoService<?, ?, ?>, ? extends GraphDrawable> getCurrentConfig() {
		return configs.get(currentConfig);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);

		// register configs
		registerConfig(new AbstractChainConfiguration(this));
		// TODO remove comment... and refactor a lot !
		registerConfig(new DSCiliaConfiguration(this));

		// selects the first config : important for providing content provider,
		// label provider... to the viewer.
		String conf = configs.keySet().iterator().next();
		updateConfigAndModel(conf, null);
	}

	@Override
	protected void onDoubleClick(Shell parentShell, Object element) {
		configs.get(currentConfig).doubleClickHandler(parentShell, element);
	}

	void setInput(Object[] elements) {
		viewer.setInput(elements);
	}

	void setContentProvider(IContentProvider contentProvider) {
		if (viewer.getContentProvider() != contentProvider) // pointer equality
			viewer.setContentProvider(contentProvider);
	}

	void setLabelProvider(IBaseLabelProvider labelProvider) {
		if (viewer.getLabelProvider() != labelProvider) // pointer equality
			viewer.setLabelProvider(labelProvider);
	}

	public void registerConfig(ChainDesignerConfiguration<? extends ChainRepoService<?, ?, ?>, ? extends GraphDrawable> config) {
		String viewID = config.getViewID();
		if (configs.get(viewID) == null) {
			configs.put(viewID, config);
			config.getRepo().registerListener(this);
			ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
			s.addSelectionListener(viewID, this);
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// TODO plus rien ne va fonctionner ici à cause du changement de
		// framework de sélection...

		// prevents storm events
		long cur = System.currentTimeMillis();
		if (cur - lastEvent < 600)
			return;

		// Assert something is selected
		Object element = null;
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			element = treeSelection.getFirstElement();
		}
		if (element == null)
			return;

		// Assert the view responsible of this selection is registered
		String id = part.getSite().getId();
		ChainDesignerConfiguration<? extends ChainRepoService<?, ?, ?>, ? extends GraphDrawable> conf = configs.get(id);
		if (conf == null)
			return;

		// Assert the element is a valid selection source
		if (conf.isValidElementForSelection(element) == false)
			return;

		// swap config if needed
		updateConfigAndModel(id, element);
		lastEvent = System.currentTimeMillis();
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		if (abstractRepoService != configs.get(currentConfig).getRepo()) // pointer
																			// equality
			return;

		// model removed checking
		for (Changeset c : changes)
			if (c.getOperation().equals(Operation.REMOVE) && c.getObject().equals(configs.get(currentConfig).getModel())) {
				configs.get(currentConfig).setModel(null);
				return;
			}

		// need update ?
		for (Changeset c : changes)
			if (c.getPath().contains(configs.get(currentConfig).getModel())) {
				configs.get(currentConfig).setModel(configs.get(currentConfig).getModel());
				return;
			}
	}

	private void updateConfigAndModel(String newConfigID, Object newModel) {

		if (configs.get(newConfigID) == null)
			return;

		currentConfig = newConfigID;
		configs.get(newConfigID).setModel(newModel);
	}
}
