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
package fr.liglab.adele.cilia.workbench.designer.parser.element.common;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;

/**
 * Represents a spec or an implementation of a Mediator.
 * 
 * @author Etienne Gandrille
 */
public interface IMediator extends IComponent {

	public List<? extends IPort> getPorts();

	public List<? extends IPort> getInPorts();

	public List<? extends IPort> getOutPorts();

	public List<? extends GenericProperty> getProperties();

	public GenericProperty getProperty(String name);

	public IScheduler getScheduler();

	public IProcessor getProcessor();

	public IDispatcher getDispatcher();

	public NameNamespaceID getId();
}
