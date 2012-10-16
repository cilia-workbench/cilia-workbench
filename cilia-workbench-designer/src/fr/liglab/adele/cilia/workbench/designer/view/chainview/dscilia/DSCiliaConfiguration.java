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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.dscilia;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.ConcreteChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.ChainDesignerConfiguration;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.ChainDesignerView;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview.DSCiliaRepositoryView;


/**
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaConfiguration extends
		ChainDesignerConfiguration<DSCiliaRepoService, ConcreteChain> {

	public DSCiliaConfiguration(ChainDesignerView parent) {
		super(parent, DSCiliaRepoService.getInstance(),
				DSCiliaRepositoryView.VIEW_ID, new DSCiliaContentProvider(),
				new DSCiliaLabelProvider());
	}

	public Object createAdapterHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	public Object deleteAdapterHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	public Object createMediatorHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	public Object deleteMediatorHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	public Object createBindingHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	public Object deleteBindingHandler(ExecutionEvent event)
			throws ExecutionException {
		// TODO Not yet implemented
		return ViewUtil.notYetImplementedHandler(event);
	}

	@Override
	public void doubleClickHandler(Shell shell, Object element) {
		// TODO Not yet implemented
	}

	@Override
	public void repositoryContentUpdated(
			AbstractRepoService<?, ?> abstractRepoService,
			List<Changeset> changes) {
		// TODO Not yet implemented
	}
}
