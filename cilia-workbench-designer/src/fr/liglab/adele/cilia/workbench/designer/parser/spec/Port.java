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

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Port {

	private String name;
	private final PortType type;
	private Node node;

	public enum PortType {
		IN("In", "in-port"), OUT("Out", "out-port");

		private String name;
		private String XMLtag;

		private PortType(String name, String XMLtag) {
			this.name = name;
			this.XMLtag = XMLtag;
		}

		public String getName() {
			return name;
		}

		public String getXMLtag() {
			return XMLtag;
		}
	}

	public Port(Node node, PortType type) throws MetadataException {
		this.node = node;
		this.type = type;
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type.getName();
	}

	@Override
	public String toString() {
		return name;
	}

	public static Node createXMLPort(Document document, Node parent, String portName, PortType portType) {
		return XMLHelpers.createNode(document, parent, portType.getXMLtag(), "name", portName);
	}
}
