/**
 * Copyright 2012-2013 France Télécom 
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

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphTextLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractBinding;

/**
 * Computes the text to be displayed in a reference architecture graph. The
 * nodes contains the default values and the bindings the cardinalities.
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainGraphTextLabelProvider implements GraphTextLabelProvider {

	@Override
	public String getNodeText(Object element, String defaultValue) {
		return defaultValue;
	}

	@Override
	public String getBindingText(EntityConnectionData element, String defaultValue) {
		EntityConnectionData ecd = (EntityConnectionData) element;
		ComponentRef src = (ComponentRef) ecd.source;
		ComponentRef dst = (ComponentRef) ecd.dest;
		// Be careful during refactoring with AbstractBinding !
		AbstractBinding binding = (AbstractBinding) src.getOutgoingBinding(dst);

		if (binding == null)
			return "";
		else {
			String from = binding.getSourceCardinality().stringId();
			String to = binding.getDestinationCardinality().stringId();
			return from + " --> " + to;
		}
	}
}
