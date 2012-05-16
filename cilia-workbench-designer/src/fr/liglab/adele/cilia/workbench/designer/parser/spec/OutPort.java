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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericOutPort;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class OutPort extends GenericOutPort implements DisplayedInPropertiesView, Identifiable, SpecPort {

	private static String XMLtag = "out-port";
	public static final String XML_ATTR_NAME = "name";

	public OutPort(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_NAME, this, "name");
	}

	public static String getXMLtag() {
		return XMLtag;
	}

	public static Node createXMLPort(Document document, Node parent, String portName) {
		return XMLHelpers.createNode(document, parent, XMLtag, XML_ATTR_NAME, portName);
	}
}
