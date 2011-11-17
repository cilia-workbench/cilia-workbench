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
 * Represents an Adapter.
 * 
 * @author Etienne Gandrille
 */
public class Adapter {

	/** The adapter name. */
	private String name;

	/** The pattern. Can be {@value #IN_PATTERN} or {@value #OUT_PATTERN}. */
	private String pattern;

	/** The collector type, for an {@value #IN_PATTERN}. */
	private String collectorType;

	/** The sender type, for an {@value #OUT_PATTERN}. */
	private String senderType;

	/** The collector or sender type. */
	private String elementType;

	/** IN pattern string, for {@link #pattern} attribute. */
	public static String IN_PATTERN = "in-only";

	/** OUT pattern string, for {@link #pattern} attribute. */
	public static String OUT_PATTERN = "out-only";

	/**
	 * Instantiates a new adapter, using reflection on the DOM model.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws MetadataException
	 *             error while parsing the XML node.
	 */
	public Adapter(Node node) throws MetadataException {

		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "pattern", this, "pattern");

		String subNodeName;
		if (pattern.equals(IN_PATTERN))
			subNodeName = "collector";
		else if (pattern.equals(OUT_PATTERN))
			subNodeName = "sender";
		else
			throw new MetadataException("Invalid pattern : " + pattern);

		Node subNode = XMLReflectionUtil.findChild(node, subNodeName);
		if (subNode == null)
			throw new MetadataException(subNodeName + " element not found");
		XMLReflectionUtil.setRequiredAttribute(subNode, "type", this, "elementType");
	}

	/**
	 * Gets the pattern. Can be {@value #IN_PATTERN} or {@value #OUT_PATTERN}.
	 * 
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
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
	 * Gets the adapter name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
