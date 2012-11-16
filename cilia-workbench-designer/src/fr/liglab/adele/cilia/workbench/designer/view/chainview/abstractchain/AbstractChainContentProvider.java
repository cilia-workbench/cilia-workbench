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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainContentProvider implements IStructuredContentProvider, IGraphEntityContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement == null)
			return new Object[0];
		else if (!(inputElement instanceof AbstractChain))
			throw new RuntimeException("input element must be an Abstract Chain");
		else
			return ((AbstractChain) inputElement).getElements();
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		List<Object> retval = new ArrayList<Object>();

		if (entity instanceof ComponentRef) {
			ComponentRef component = (ComponentRef) entity;
			Binding[] bindings = component.getOutgoingBindings();
			for (Binding binding : bindings) {
				ComponentRef ro = binding.getDestinationComponent();
				if (ro != null)
					retval.add(ro);
			}
		}

		return retval.toArray();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}
}
