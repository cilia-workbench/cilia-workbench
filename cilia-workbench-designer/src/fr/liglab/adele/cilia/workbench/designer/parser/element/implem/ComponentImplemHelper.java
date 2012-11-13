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
package fr.liglab.adele.cilia.workbench.designer.parser.element.implem;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IPort;

/**
 * 
 * @author Etienne Gandrille
 */
public class ComponentImplemHelper {

	public static List<IPort> getPorts(Node node) throws CiliaException {
		List<IPort> ports = new ArrayList<IPort>();

		Node portsNode = XMLHelpers.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLHelpers.findChildren(portsNode, InPortImplem.XML_NODE_NAME);
			for (Node inPort : inPorts)
				ports.add(new InPortImplem(inPort));
			Node[] outPorts = XMLHelpers.findChildren(portsNode, OutPortImplem.XML_NODE_NAME);
			for (Node outPort : outPorts)
				ports.add(new OutPortImplem(outPort));
		}

		return ports;
	}
}
