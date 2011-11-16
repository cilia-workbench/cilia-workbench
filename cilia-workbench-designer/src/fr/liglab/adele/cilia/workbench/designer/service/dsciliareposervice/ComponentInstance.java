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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;

/**
 * An abstract class for {@link MediatorInstance} and {@link AdapterInstance}.
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentInstance {

	/** The component id. */
	protected String id;

	/** The component type. */
	protected String type;

	/**
	 * Instantiates a new component instance, using reflection on the DOM node.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws MetadataException
	 *             XML parsing error, or reflexion error.
	 */
	public ComponentInstance(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "id", this, "id");
		XMLReflectionUtil.setRequiredAttribute(node, "type", this, "type");
	}

	/**
	 * Gets the component id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the component type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
