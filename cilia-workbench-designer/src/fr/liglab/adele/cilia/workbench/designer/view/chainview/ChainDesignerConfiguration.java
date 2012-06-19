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
package fr.liglab.adele.cilia.workbench.designer.view.chainview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.IRepoServiceListener;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainDesignerConfiguration<RepoType extends AbstractRepoService<?, ?>, ModelType extends GraphDrawable>
		implements IRepoServiceListener {

	protected ModelType model = null;

	private final ChainDesignerView parent;

	private final RepoType repo;

	private final String viewID;

	private final AbstractGraphContentProvider<ModelType> contentProvider;

	private final AbstractGraphLabelProvider labelProvider;

	public ChainDesignerConfiguration(ChainDesignerView parent, RepoType repo, String viewID,
			AbstractGraphContentProvider<ModelType> contentProvider, AbstractGraphLabelProvider labelProvider) {
		this.parent = parent;
		this.repo = repo;
		this.viewID = viewID;
		this.contentProvider = contentProvider;
		this.labelProvider = labelProvider;
	}

	public String getDefaultName() {
		return "Chain designer view";
	}

	public static ChainDesignerView getChainDesignerView(ExecutionEvent event) {
		return (ChainDesignerView) ViewUtil.findViewWithId(event, ChainDesignerView.viewId);
	}

	public ModelType getModel() {
		return model;
	}

	public String getViewID() {
		return viewID;
	}

	public RepoType getRepo() {
		return repo;
	}

	public IContentProvider getContentProvider() {
		return contentProvider;
	}

	public IBaseLabelProvider getLabelProvider() {
		return labelProvider;
	}

	@SuppressWarnings("unchecked")
	protected void setModel(Object element) {

		this.model = (ModelType) element;

		contentProvider.setModel(model);
		if (model != null) {
			parent.setInput(model.getElements());
			parent.setViewName(model.getName());
		} else {
			parent.setInput(new Object[0]);
			parent.setViewName(getDefaultName());
		}
		ToggleSourceProvider.setToggleVariable(ToolbarEnabler.VARIABLE_NAME, model != null);
		parent.refresh();
	}

	public boolean isValidElementForSelection(Object element) {
		try {
			@SuppressWarnings({ "unused", "unchecked" })
			ModelType m = (ModelType) element;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public abstract Object createAdapterHandler(ExecutionEvent event) throws ExecutionException;

	public abstract Object deleteAdapterHandler(ExecutionEvent event) throws ExecutionException;

	public abstract Object createMediatorHandler(ExecutionEvent event) throws ExecutionException;

	public abstract Object deleteMediatorHandler(ExecutionEvent event) throws ExecutionException;

	public abstract Object createBindingHandler(ExecutionEvent event) throws ExecutionException;

	public abstract Object deleteBindingHandler(ExecutionEvent event) throws ExecutionException;

	public abstract void doubleClickHandler(Shell shell, Object element);
}
