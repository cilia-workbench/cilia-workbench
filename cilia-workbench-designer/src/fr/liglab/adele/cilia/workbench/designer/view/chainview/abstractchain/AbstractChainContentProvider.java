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

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ComponentRef;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.GraphContentProvider;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainContentProvider extends GraphContentProvider<AbstractChain> {

	@Override
	public Object[] getConnectedTo(Object entity) {
		List<Object> retval = new ArrayList<Object>();

		if (getModel() != null && entity instanceof ComponentRef) {
			@SuppressWarnings("unchecked")
			ComponentRef<AbstractChain> component = (ComponentRef<AbstractChain>) entity;
			Binding[] bindings = component.getOutgoingBindings();
			for (Binding binding : bindings) {
				ComponentRef<?> ro = binding.getDestinationComponent();
				if (ro != null)
					retval.add(ro);
			}
		}

		return retval.toArray();
	}
}
