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

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain.AbstractChainConfiguration;

/**
 * 
 * @author Etienne Gandrille
 */
public class ChainDesignerView extends GraphView implements IRepoServiceListener, ISelectionListener {

	public static final String viewId = "fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview";

	private Shell parentShell;

	private Map<String, ChainDesignerConfiguration<AbstractCompositionsRepoService, AbstractChain>> configs = new HashMap<String, ChainDesignerConfiguration<AbstractCompositionsRepoService, AbstractChain>>();
	String currentConfig = null;

	public ChainDesignerView() {
	}

	public ChainDesignerConfiguration<AbstractCompositionsRepoService, AbstractChain> getCurrentConfig() {
		return configs.get(currentConfig);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);
		parentShell = ViewUtil.getShell(parent);

		// register configs
		registerConfig(new AbstractChainConfiguration(this));

		// selects the first config : important for providing content provider,
		// label provider... to the viewer.
		String conf = configs.keySet().iterator().next();
		changeConfig(conf);
		configs.get(conf).setModel(null);

		// double click listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object element = getFirstSelectedElement();
				if (element != null)
					configs.get(currentConfig).doubleClickHandler(parentShell, element);
			}
		});
	}

	void setInput(Object[] elements) {
		viewer.setInput(elements);
	}

	void refresh() {
		viewer.refresh();
	}

	void setViewName(String name) {
		setPartName(name);
	}

	public void registerConfig(ChainDesignerConfiguration<AbstractCompositionsRepoService, AbstractChain> config) {
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
		ChainDesignerConfiguration<AbstractCompositionsRepoService, AbstractChain> conf = configs.get(id);
		if (conf == null)
			return;

		// Assert the element is a valid selection source
		if (conf.isValidElementForSelection(element) == false)
			return;

		// swap config if needed
		changeConfig(id);

		// update input
		conf.setModel(element); // performs refresh
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		if (abstractRepoService != configs.get(currentConfig).getRepo()) // pointer
																			// equality
			return;

		// model removed checking
		for (Changeset c : changes)
			if (c.getOperation().equals(Operation.REMOVE)
					&& c.getObject().equals(configs.get(currentConfig).getModel())) {
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

	private void changeConfig(String newConfig) {
		if (configs.get(newConfig) == null)
			return;
		if (newConfig.equals(currentConfig))
			return;

		currentConfig = newConfig;

		setViewName(configs.get(newConfig).getDefaultName());
		viewer.setContentProvider(configs.get(newConfig).getContentProvider());
		viewer.setLabelProvider(configs.get(newConfig).getLabelProvider());
		viewer.setInput(new Object[0]);
	}
}
