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
 * Represents a Processor.
 * 
 * @author Etienne Gandrille
 */
public class Processor {

	/** The name. */
	private String name;

	/** The classname. */
	private String classname;

	/** The namespace. */
	private String namespace;

	/** The method name. */
	private String methodName;

	/** The method data type. */
	private String methodDataType;

	/**
	 * Instantiates a new processor, using reflection on the DOM model.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws MetadataException
	 *             error while parsing the XML node.
	 */
	public Processor(Node node) throws MetadataException {

		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "classname", this, "classname");
		XMLReflectionUtil.setOptionalAttribute(node, "namespace", this, "namespace");

		Node methodNode = XMLReflectionUtil.findChild(node, "method");
		if (methodNode == null)
			throw new MetadataException("method element not found");
		XMLReflectionUtil.setRequiredAttribute(methodNode, "name", this, "methodName");
		XMLReflectionUtil.setRequiredAttribute(methodNode, "data.type", this, "methodDataType");
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
}
