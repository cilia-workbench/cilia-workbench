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
package fr.liglab.adele.cilia.workbench.common.misc;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Etienne Gandrille
 */
public class XMLUtil {

	public static Node[] findXMLChildNode(Node root, String nodeName) {
		return findXMLChildNodeInternal(root, nodeName);
	}

	public static Node[] findXMLChildNode(Node root, String nodeName, String nodeAttribute, String attributeValue) {
		return findXMLChildNodeInternal(root, nodeName, nodeAttribute, attributeValue);
	}

	public static Node[] findXMLChildNode(Node root, String nodeName, String nodeAttribute1, String attributeValue1,
			String nodeAttribute2, String attributeValue2) {
		return findXMLChildNodeInternal(root, nodeName, nodeAttribute1, attributeValue1, nodeAttribute2,
				attributeValue2);
	}

	private static Node[] findXMLChildNodeInternal(Node root, String nodeName, String... attr_value) {
		ArrayList<Node> retval = new ArrayList<Node>();

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeName().equalsIgnoreCase(nodeName)) {
				boolean ok_node = true;

				for (int idx = 0; idx < attr_value.length && ok_node == true; idx += 2) {
					String nodeAttribute = attr_value[idx];
					String attributeValue = attr_value[idx + 1];

					NamedNodeMap attrs = current.getAttributes();

					boolean ok_attr = false;
					for (int j = 0; j < attrs.getLength() && ok_attr == false; j++) {
						Node attr = attrs.item(j);
						String name = attr.getNodeName();
						String value = attr.getNodeValue();
						if (name.equalsIgnoreCase(nodeAttribute) && value.equals(attributeValue))
							ok_attr = true;
					}

					if (ok_attr == false)
						ok_node = false;
				}

				if (ok_node == true)
					retval.add(current);
			}
		}

		return retval.toArray(new Node[0]);
	}

	public static Node getOrCreateSubNode(Document document, Node root, String subNodeName) {
		Node[] mediatorsNodes = XMLUtil.findXMLChildNode(root, subNodeName);
		Node mediatorNode = null;
		if (mediatorsNodes.length == 0) {
			Element child = document.createElement(subNodeName);
			mediatorNode = root.appendChild(child);
		} else {
			mediatorNode = mediatorsNodes[0];
		}

		return mediatorNode;
	}

	public static String computeNamespace(String name) {
		return StringUtil.getBeforeSeparatorOrNothing(name);
	}

	public static String computeName(String name) {
		return StringUtil.getAfterSeparatorOrAll(name);
	}
}
