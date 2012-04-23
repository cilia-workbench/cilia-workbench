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

import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.marker.MarkerFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Adapter implements DisplayedInPropertiesView, MarkerFinder {

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
	
	@Override
	public void createMarkers(Object rootSourceProvider) {
		
		// name validation
		if (name == null || name.length() == 0)
			CiliaMarkerUtil.createErrorMarker("name can't be null or empty", JarRepoService.getInstance(), this);
		
		// pattern and element type validation
		try {
			// pattern validation
			getSubNode(node, pattern);
			
			// element type validation
			if (elementType == null || elementType.length() == 0)
				CiliaMarkerUtil.createErrorMarker("Element type is undefined", JarRepoService.getInstance(), this);
		} catch (MetadataException e) {
			CiliaMarkerUtil.createErrorMarker(e.getMessage(), JarRepoService.getInstance(), this);
		} 
	}
}
