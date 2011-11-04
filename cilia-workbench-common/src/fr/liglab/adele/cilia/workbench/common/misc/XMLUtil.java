package fr.liglab.adele.cilia.workbench.common.misc;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Strings;

public class XMLUtil {
	
	public static Node[] findXMLChildNode(Node root, String nodeName) {
		return findXMLChildNode(root, nodeName, null, null);
	}
	
	
	public static Node[] findXMLChildNode(Node root, String nodeName, String nodeAttribute, String attributeValue) {
		ArrayList<Node> retval = new ArrayList<Node>();

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeName().equalsIgnoreCase(nodeName)) {
				if (Strings.isNullOrEmpty(nodeAttribute)) {
					retval.add(current);
				} else {
					NamedNodeMap attrs = current.getAttributes();
					for (int j = 0; j < attrs.getLength(); j++) {
						Node attr = attrs.item(j);
						String name = attr.getNodeName();
						String value = attr.getNodeValue();
						if (name.equalsIgnoreCase(nodeAttribute) && value.equals(attributeValue))
							retval.add(current);
					}
				}
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
		}
		else {
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