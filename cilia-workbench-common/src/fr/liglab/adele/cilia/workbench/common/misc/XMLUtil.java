package fr.liglab.adele.cilia.workbench.common.misc;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A few methods, for managing XML files using DOM.
 */
public class XMLUtil {

	/**
	 * Finds child nodes which a specific node name, and optionnaly a set of
	 * attribute values pairs.
	 * 
	 * @param root
	 *            the root node
	 * @param childName
	 *            the child name, to be searched
	 * @param attrVal
	 *            a set of attribute name and value
	 * 
	 * @return a child node list.
	 */
	public static Node[] findXMLChildNode(Node root, String childName, String... attrVal) {
		ArrayList<Node> retval = new ArrayList<Node>();

		if (attrVal.length % 2 != 0)
			throw new IllegalArgumentException("Please give name and value pairs for attrVal parameter");

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeName().equalsIgnoreCase(childName)) {
				int idx = 0;
				boolean continue_parsing = true;

				while (idx < attrVal.length && continue_parsing) {
					NamedNodeMap attrs = current.getAttributes();
					boolean found = false;
					for (int j = 0; j < attrs.getLength(); j++) {
						Node attr = attrs.item(j);
						String name = attr.getNodeName();
						String value = attr.getNodeValue();
						if (name.equalsIgnoreCase(attrVal[idx]) && value.equals(attrVal[idx + 1]))
							found = true;
					}

					if (found == false)
						continue_parsing = false;

					idx += 2;
				}

				if (continue_parsing)
					retval.add(current);
			}
		}

		return retval.toArray(new Node[0]);
	}

	/**
	 * Gets a child node using its name, or create it if it doesn't exists.
	 * 
	 * @param document
	 *            the XML document
	 * @param root
	 *            the root node
	 * @param subNodeName
	 *            the child node name, to be search
	 * @return the found or created child node.
	 */
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

	/**
	 * Compute namespace form a node name.
	 * 
	 * @param name
	 *            the qualified node name
	 * @return the namespace, which can be empty.
	 */
	public static String computeNamespace(String name) {
		return StringUtil.getBeforeSeparatorOrNothing(name);
	}

	/**
	 * Compute name form a node name (removes namespace).
	 * 
	 * @param name
	 *            the qualified node name
	 * @return the node name, without namespace.
	 */
	public static String computeName(String name) {
		return StringUtil.getAfterSeparatorOrAll(name);
	}
}
