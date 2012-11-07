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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.dsciliachain;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.DeleteMediatorDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeleteMediatorHandler extends DSCiliaChainHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DSCiliaChain model = getDisplayedModel(event);

		if (model != null) {
			DeleteMediatorDialog window = new DeleteMediatorDialog(ViewUtil.getShell(event), model);
			if (window.open() == Window.OK) {
				Object[] objects = window.getResult();
				if (objects.length != 0) {
					MediatorRef component = (MediatorRef) objects[0];
					try {
						DSCiliaRepoService.getInstance().deleteComponent(model, component);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}
}
