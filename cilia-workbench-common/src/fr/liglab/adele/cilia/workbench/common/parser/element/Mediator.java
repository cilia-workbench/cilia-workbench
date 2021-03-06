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
package fr.liglab.adele.cilia.workbench.common.parser.element;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;

/**
 * Represents a spec or an implementation of a Mediator.
 * 
 * @author Etienne Gandrille
 */
public abstract class Mediator extends ComponentDefinition {

	public abstract List<? extends Property> getProperties();

	public Mediator(NameNamespaceID id, List<Port> ports, String physicalResourcePath) {
		super(id, ports, physicalResourcePath);
	}

	public Property getProperty(String key) {
		for (Property p : getProperties())
			if (p.getName().equalsIgnoreCase(key))
				return p;
		return null;
	}

	public abstract Scheduler getScheduler();

	public abstract Processor getProcessor();

	public abstract Dispatcher getDispatcher();
}
