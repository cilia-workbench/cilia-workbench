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
package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.CommonHandler;
import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

/**
 * Handler called when creating a mediator in the chain designer view.
 * 
 * @author Etienne Gandrille
 */
public class CreateMediatorHandler extends CommonHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Chain chain = getChainDesignerView(event).getModel();
		if (chain != null) {
			NewMediatorInstanceWindow window = new NewMediatorInstanceWindow(getShell(event), chain);
			if (window.open() == Window.OK) {
				String id = window.getComponentId();
				String type = window.getComponentType();
				try {
					DsciliaRepoService.getInstance().createMediatorInstance(chain, id, type);
				} catch (MetadataException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
