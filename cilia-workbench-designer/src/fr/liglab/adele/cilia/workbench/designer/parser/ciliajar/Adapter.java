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

import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Adapter implements DisplayedInPropertiesView {

	private String name;
	private String pattern;

	private String collectorType;
	private String senderType;
	private String elementType;

	public static String IN_PATTERN = "in-only";
	public static String OUT_PATTERN = "out-only";

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

		Node subNode = XMLHelpers.findChild(node, subNodeName);
		if (subNode == null)
			throw new MetadataException(subNodeName + " element not found");
		XMLReflectionUtil.setRequiredAttribute(subNode, "type", this, "elementType");
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
}
