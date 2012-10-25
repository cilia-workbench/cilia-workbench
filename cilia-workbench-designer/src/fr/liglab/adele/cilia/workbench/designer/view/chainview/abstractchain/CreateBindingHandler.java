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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.dialog.NewBindingDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class CreateBindingHandler extends AbstractChainHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AbstractChain model = getDisplayedModel(event);

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
					AbstractCompositionsRepoService.getInstance().createBinding(model, srcElem, srcPort, dstElem, dstPort, srcCard, dstCard);
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
