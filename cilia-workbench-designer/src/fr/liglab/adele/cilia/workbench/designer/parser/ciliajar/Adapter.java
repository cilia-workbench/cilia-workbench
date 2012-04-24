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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Adapter implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	private Node node;

	private String name;
	private String pattern;
	// collector or sender type
	private String elementType;

	public static String IN_PATTERN = "in-only";
	public static String OUT_PATTERN = "out-only";

	public Adapter(Node node) throws MetadataException {

		this.node = node;
		XMLReflectionUtil.setAttribute(node, "name", this, "name");
		XMLReflectionUtil.setAttribute(node, "pattern", this, "pattern");

		try {
			Node subNode = getSubNode(node, pattern);
			XMLReflectionUtil.setAttribute(subNode, "type", this, "elementType");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	private static Node getSubNode(Node rootNode, String pattern) throws MetadataException {
		String subNodeName;
		if (pattern.equals(IN_PATTERN))
			subNodeName = "collector";
		else if (pattern.equals(OUT_PATTERN))
			subNodeName = "sender";
		else
			throw new MetadataException("Invalid pattern : " + pattern);

		Node subNode = XMLHelpers.findChild(rootNode, subNodeName);

		if (subNode == null)
			throw new MetadataException("Adapter with pattern " + pattern + " must have a " + subNodeName);

		return subNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder
	 * #createErrorsAndWarnings(java.lang.Object)
	 */
	@Override
	public CiliaFlag[] getErrorsAndWarnings() {

		CiliaError e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaError e2 = null;
		CiliaError e3 = null;

		// pattern and element type validation
		try {
			// pattern validation
			getSubNode(node, pattern);
			// element type validation
			e2 = CiliaError.checkStringNotNullOrEmpty(this, elementType, "elementType");
		} catch (MetadataException e) {
			e3 = new CiliaError(e.getMessage(), this);
		}

		return CiliaFlag.generateTab(e1, e2, e3);
	}
}
