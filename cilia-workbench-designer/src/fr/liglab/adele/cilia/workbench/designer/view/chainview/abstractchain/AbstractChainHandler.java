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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractChainHandler extends AbstractHandler {

	public static AbstractChainView getAbstractChainView(ExecutionEvent event) {
		return (AbstractChainView) ViewUtil.findViewWithId(event, AbstractChainView.VIEW_ID);
	}

	public static AbstractChain getDisplayedModel(ExecutionEvent event) {
		AbstractChainView view = getAbstractChainView(event);
		return view.getModel();
	}

}
