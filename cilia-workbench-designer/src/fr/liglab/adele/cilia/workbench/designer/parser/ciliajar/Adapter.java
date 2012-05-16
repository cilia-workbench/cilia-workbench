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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Adapter extends NameNamespace implements DisplayedInPropertiesView {

	private Node node;

	private String pattern;
	// collector or sender type
	private String elementType;

	public static String IN_PATTERN = "in-only";
	public static String OUT_PATTERN = "out-only";

	public Adapter(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, "name", this, "name");
		ReflectionUtil.setAttribute(node, "namespace", this, "namespace", CiliaConstants.getDefaultNamespace());

		this.node = node;
		ReflectionUtil.setAttribute(node, "pattern", this, "pattern");

		try {
			Node subNode = getSubNode(node, pattern);
			ReflectionUtil.setAttribute(subNode, "type", this, "elementType");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPattern() {
		return pattern;
	}

	public boolean isInAdapter() {
		return pattern.equals(IN_PATTERN);
	}

	public boolean isOutAdapter() {
		return pattern.equals(OUT_PATTERN);
	}

	private static Node getSubNode(Node rootNode, String pattern) throws CiliaException {
		String subNodeName;
		if (pattern.equals(IN_PATTERN))
			subNodeName = "collector";
		else if (pattern.equals(OUT_PATTERN))
			subNodeName = "sender";
		else
			throw new CiliaException("Invalid pattern : " + pattern);

		Node subNode = XMLHelpers.findChild(rootNode, subNodeName);

		if (subNode == null)
			throw new CiliaException("Adapter with pattern " + pattern + " must have a " + subNodeName);

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
		CiliaFlag[] flagsTab = super.getErrorsAndWarnings();

		CiliaError e1 = null;
		CiliaError e2 = null;

		// pattern and element type validation
		try {
			// pattern validation
			getSubNode(node, pattern);
			// element type validation
			e1 = CiliaError.checkStringNotNullOrEmpty(this, elementType, "elementType");
		} catch (CiliaException e) {
			e2 = new CiliaError(e.getMessage(), this);
		}

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}
