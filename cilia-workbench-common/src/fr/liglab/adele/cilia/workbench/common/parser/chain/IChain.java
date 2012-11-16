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

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.parser.element.IComponent;

/**
 * 
 * @author Etienne Gandrille
 */
public interface IChain extends Identifiable {

	public List<? extends AdapterRef> getAdapters();

	public List<? extends MediatorRef> getMediators();

	public List<? extends Binding> getBindings();

	public ComponentRef getComponent(String componentId);

	public ComponentRef[] getComponents();

	/**
	 * Finds the {@link IComponent} referenced by the chain component with id
	 * given into parameter. If the component can't be located, throws an
	 * exception containing an error message.
	 * 
	 * @param componentID
	 * @return
	 * @throws CiliaException
	 */
	public IComponent getReferencedComponent(String componentID) throws CiliaException;
}