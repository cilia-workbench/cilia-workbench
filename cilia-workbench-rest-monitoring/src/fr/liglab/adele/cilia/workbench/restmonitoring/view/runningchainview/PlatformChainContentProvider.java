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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.BindingInstance;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.MediatorInstance;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChainContentProvider implements IGraphEntityContentProvider {

	private final PlatformChain model;

	public PlatformChainContentProvider(PlatformChain model) {
		this.model = model;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == null)
			return new Object[0];
		else {
			List<Object> retval = new ArrayList<Object>();
			PlatformChain chain = (PlatformChain) inputElement;
			retval.addAll(chain.getAdapters());
			retval.addAll(chain.getMediators());

			return retval.toArray();
		}
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		if (entity == null)
			return new Object[0];
		else if (entity instanceof Adapter) {
			List<Object> retval = new ArrayList<Object>();
			List<BindingInstance> bindings = model.getOutgoingBindings((String) ((Adapter) entity).getId());
			for (BindingInstance binding : bindings)
				retval.add(model.getComponent(binding.getDestinationId()));
			return retval.toArray();
		} else if (entity instanceof MediatorInstance) {
			List<Object> retval = new ArrayList<Object>();
			List<BindingInstance> bindings = model.getOutgoingBindings(((MediatorInstance) entity).getName());
			for (BindingInstance binding : bindings)
				retval.add(model.getComponent(binding.getDestinationId()));
			return retval.toArray();
		} else
			throw new RuntimeException("Unsupported class " + entity.getClass());
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
