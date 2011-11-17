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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.common.XMLReflectionUtil;

/**
 * Represents an abstarct generic Port. This method inteds to be subclassed in
 * {@link InPort} and {@link OutPort}.
 * 
 * @author Etienne Gandrille
 */
public abstract class Port {

	/** The port name. */
	private String name;

	/**
	 * Instantiates a new Port, using reflection on the DOM model.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws MetadataException
	 *             error while parsing the XML node.
	 */
	public Port(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
