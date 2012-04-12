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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.marker.MarkerFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Property implements DisplayedInPropertiesView, MarkerFinder {

	public static final String XML_NODE_NAME = "property";

	public static final String XML_ATTR_KEY = "key";
	private String key;
	public static final String XML_ATTR_VALUE = "value";
	private String value;
	private Node node;

	public Property(Node node) throws MetadataException {
		this.node = node;
		XMLReflectionUtil.setRequiredAttribute(node, XML_ATTR_KEY, this, "key");
		XMLReflectionUtil.setRequiredAttribute(node, XML_ATTR_VALUE, this, "value");
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (key == null || key.length() == 0)
			return "<undefined> = " + value;
		if (value == null || value.length() == 0)
			return key + " = <undefined>";

		return key + " = " + value;
	}	

	public Changeset[] merge(Property newInstance) {
		if (newInstance.getValue().equals(value))
			return new Changeset[0];
		else {
			value = newInstance.getValue();
			Changeset c = new Changeset(Operation.UPDATE, this);
			Changeset[] retval = new Changeset[1];
			retval[0] = c;
			return retval;
		}
	}

	public static Node createXMLProperty(Document document, Node parent, String key, String value) {
		return XMLHelpers.createNode(document, parent, XML_NODE_NAME, XML_ATTR_KEY, key, XML_ATTR_VALUE, value);
	}
	
	@Override
	public void createMarkers(Object rootSourceProvider) {
		if (key == null || key.isEmpty())
			CiliaMarkerUtil.createErrorMarker("Property does't have its key defined", SpecRepoService.getInstance(), this);
		if (value == null || value.isEmpty())
			CiliaMarkerUtil.createWarningMarker("Property " + key + " does't have its value defined", SpecRepoService.getInstance(), this);
	}
}
