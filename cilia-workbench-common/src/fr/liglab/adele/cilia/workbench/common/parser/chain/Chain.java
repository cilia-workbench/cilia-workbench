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
package fr.liglab.adele.cilia.workbench.common.parser.chain;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.parser.element.Component;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Chain implements Identifiable {

	public abstract List<? extends AdapterRef> getAdapters();

	public abstract List<? extends MediatorRef> getMediators();

	public abstract List<? extends Binding> getBindings();

	/**
	 * Finds the {@link Component} referenced by the chain component with id
	 * given into parameter. If the component can't be located, throws an
	 * exception containing an error message.
	 * 
	 * @param componentID
	 * @return
	 * @throws CiliaException
	 */
	public abstract Component getReferencedComponent(String componentID) throws CiliaException;

	public ComponentRef getComponent(String componentId) {
		for (AdapterRef adapter : getAdapters())
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorRef mediator : getMediators())
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	public ComponentRef[] getComponents() {
		List<ComponentRef> retval = new ArrayList<ComponentRef>();
		for (AdapterRef adapter : getAdapters())
			retval.add(adapter);
		for (MediatorRef mediator : getMediators())
			retval.add(mediator);
		return retval.toArray(new ComponentRef[0]);
	}
}