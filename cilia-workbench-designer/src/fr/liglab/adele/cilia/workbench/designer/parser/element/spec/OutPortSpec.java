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
package fr.liglab.adele.cilia.workbench.designer.parser.element.spec;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutPort;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class OutPortSpec extends OutPort implements DisplayedInPropertiesView, Identifiable {

	private static String XML_TAG = "out-port";
	public static final String XML_ATTR_NAME = "name";

	public OutPortSpec(Node node) throws CiliaException {
		super(XMLHelpers.findAttributeValueOrEmpty(node, "name"), XMLHelpers.findAttributeValueOrEmpty(node, "type"));
	}

	public static String getXMLtag() {
		return XML_TAG;
	}

	public static Node createXMLPort(Document document, Node parent, String portName) {
		// TODO update port creation
		return XMLHelpers.createChild(document, parent, XML_TAG, XML_ATTR_NAME, portName);
	}
}
