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
package fr.liglab.adele.cilia.workbench.common.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * Static methods, for managing XML files.
 * 
 * @author Etienne Gandrille
 */
public class XMLHelpers {

	/***
	 * Gets the namespace part from a node name.
	 * 
	 * @param name
	 *            the node name
	 * @return the namespace part
	 */
	public static String computeNamespace(String name) {
		return XMLStringUtil.getBeforeSeparatorOrNothing(name);
	}

	/**
	 * Gets the name part from a node name, removing the namespace part if it
	 * exists.
	 * 
	 * @param name
	 * @return the name, without namespace
	 */
	public static String computeName(String name) {
		return XMLStringUtil.getAfterSeparatorOrAll(name);
	}

	/**
	 * Gets a {@link Document} from a file.
	 * 
	 * @param file
	 *            the file
	 * @return the dom document
	 * @throws CiliaException
	 *             if any error during parsing.
	 */
	public static Document getDocument(File file) throws CiliaException {
		try {
			return getDocumentBuilder().parse(file);
		} catch (SAXException e) {
			throw new CiliaException("Can't parse document from file " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new CiliaException("Can't parse document from file " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Gets a {@link Document} from an {@link InputStream}.
	 * 
	 * @param is
	 *            the input stream
	 * @return the dom document
	 * @throws CiliaException
	 *             if any error during parsing.
	 */
	public static Document getDocument(InputStream is) throws CiliaException {
		try {
			return getDocumentBuilder().parse(is);
		} catch (SAXException e) {
			throw new CiliaException("Can't parse document from stream", e);
		} catch (IOException e) {
			throw new CiliaException("Can't parse document from stream", e);
		}
	}

	private static DocumentBuilder getDocumentBuilder() throws CiliaException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new CiliaException("Can't get document builder ", e);
		}

		return builder;
	}

	/**
	 * Gets an Input stream from a file embedded in a jar archive.
	 * 
	 * @param jarFile
	 *            the jar archive file, on the hard disk.
	 * @param fileName
	 *            the file name, in the archive. The file must be located at the
	 *            archive root.
	 * @return the input stream
	 * @throws CiliaException
	 *             if any error.
	 */
	public static InputStream inputStreamFromFileInJarArchive(File jarFile, String fileName) throws CiliaException {
		// Jar file
		JarFile file;
		try {
			file = new JarFile(jarFile);
		} catch (IOException e) {
			throw new CiliaException("Can't open jar file " + jarFile.getAbsolutePath(), e);
		}

		// File
		ZipEntry entry = file.getEntry(fileName);
		if (entry == null)
			throw new CiliaException("File " + fileName + " not found in " + jarFile.getAbsolutePath());

		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			throw new CiliaException("Can't access file " + fileName + " in jar file " + jarFile.getAbsolutePath(), e);
		}

		return is;
	}

	/**
	 * Writes a document using its DOM representation.
	 * 
	 * @param document
	 *            the document
	 * @param file
	 *            the file, on the local file system.
	 * @throws CiliaException
	 *             the metadata exception
	 */
	public static void writeDOM(Document document, File file) throws CiliaException {
		Source source = new DOMSource(document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer xformer = transformerFactory.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(file);
			xformer.transform(source, result);
		} catch (TransformerException e) {
			throw new CiliaException("XML transformer error", e);
		}
	}

	/**
	 * Gets the root node in a document.
	 * 
	 * @param document
	 *            the document
	 * @param nodeName
	 *            the node name
	 * @return the root node
	 * @throws CiliaException
	 *             if the root node doesn't match nodeName.
	 */
	public static Node getRootNode(Document document, String nodeName) throws CiliaException {
		NodeList nodes = document.getChildNodes();

		if (nodes != null) {
			for (int i = 0; i < nodes.getLength(); i++)
				if (nodes.item(i).getNodeName().equalsIgnoreCase(nodeName))
					return nodes.item(i);
		}

		throw new CiliaException("Can't find root node " + nodeName);
	}

	public static Node getOrCreateChild(Document document, Node parent, String childNodeName) {
		Node dstNode = findChild(parent, childNodeName);
		if (dstNode == null) {
			dstNode = createChild(document, parent, childNodeName);
		}
		return dstNode;
	}

	public static Node createChild(Document document, Node parent, String childNodeName) {
		return createChildInternal(document, parent, childNodeName);
	}

	public static Node createChild(Document document, Node parent, String nodeName, String attrName, String attrValue) {
		return createChildInternal(document, parent, nodeName, attrName, attrValue);
	}

	public static Node createChild(Document document, Node parent, String nodeName, String attrName1,
			String attrValue1, String attrName2, String attrValue2) {
		return createChildInternal(document, parent, nodeName, attrName1, attrValue1, attrName2, attrValue2);
	}

	private static Node createChildInternal(Document document, Node parent, String nodeName,
			String... attr_name_and_value) {
		Element newNode = document.createElement(nodeName);

		for (int i = 0; i < attr_name_and_value.length; i += 2) {
			String attrName = attr_name_and_value[i];
			String attrValue = attr_name_and_value[i + 1];
			newNode.setAttribute(attrName, attrValue);
		}
		parent.appendChild(newNode);
		return newNode;
	}

	public static String findAttributeValue(Node node, String attrName) throws CiliaException {

		Map<String, String> attrMap = findAttributesValues(node);

		for (String attr : attrMap.keySet()) {
			String fullname = attr.toLowerCase();
			String name = computeName(fullname);
			// String namespace = computeNamespace(fullname);
			String value = attrMap.get(attr);

			if (attrName.equals(name))
				return value;
		}

		throw new CiliaException("Attribute " + attrName + " not found");
	}

	public static Map<String, String> findAttributesValues(Node node) {

		Map<String, String> retval = new HashMap<String, String>();
		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				String name = attr.getNodeName();
				String value = attr.getNodeValue();

				retval.put(name, value);
			}
		}

		return retval;
	}

	public static String findAttributeValue(Node node, String attrName, String defaultValue) throws CiliaException {
		try {
			return findAttributeValue(node, attrName);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Node findChild(Node node, String childName) {
		Node[] children = findChildren(node, childName);
		if (children.length == 0)
			return null;
		else
			return children[0];
	}

	public static Node[] findChildren(Node root, String nodeName) {
		return findChildrenInternal(root, nodeName);
	}

	public static Node[] findChildren(Node root, String nodeName, String attributeName, String attributeValue) {
		return findChildrenInternal(root, nodeName, attributeName, attributeValue);
	}

	public static Node[] findChildren(Node root, String nodeName, String attributeName1, String attributeValue1,
			String attributeName2, String attributeValue2) {
		return findChildrenInternal(root, nodeName, attributeName1, attributeValue1, attributeName2, attributeValue2);
	}

	private static Node[] findChildrenInternal(Node root, String nodeName, String... attr_value) {
		ArrayList<Node> retval = new ArrayList<Node>();

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE
					&& computeName(current.getNodeName()).equalsIgnoreCase(nodeName)) {
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

	/**
	 * Convert a node type to a string. For debug purpose only.
	 * 
	 * @param nodeType
	 *            the node type
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String nodeTypeToString(short nodeType) throws Exception {
		if (nodeType == Node.ELEMENT_NODE)
			return "ELEMENT_NODE";
		if (nodeType == Node.ATTRIBUTE_NODE)
			return "ATTRIBUTE_NODE";
		if (nodeType == Node.TEXT_NODE)
			return "TEXT_NODE";
		if (nodeType == Node.CDATA_SECTION_NODE)
			return "CDATA_SECTION_NODE";
		if (nodeType == Node.ENTITY_REFERENCE_NODE)
			return "ENTITY_REFERENCE_NODE";
		if (nodeType == Node.ENTITY_NODE)
			return "ENTITY_NODE";
		if (nodeType == Node.PROCESSING_INSTRUCTION_NODE)
			return "PROCESSING_INSTRUCTION_NODE";
		if (nodeType == Node.COMMENT_NODE)
			return "COMMENT_NODE";
		if (nodeType == Node.DOCUMENT_NODE)
			return "DOCUMENT_NODE";
		if (nodeType == Node.DOCUMENT_TYPE_NODE)
			return "DOCUMENT_TYPE_NODE";
		if (nodeType == Node.DOCUMENT_FRAGMENT_NODE)
			return "DOCUMENT_FRAGMENT_NODE";
		if (nodeType == Node.NOTATION_NODE)
			return "NOTATION_NODE";
		if (nodeType == Node.DOCUMENT_POSITION_DISCONNECTED)
			return "DOCUMENT_POSITION_DISCONNECTED";
		if (nodeType == Node.DOCUMENT_POSITION_PRECEDING)
			return "DOCUMENT_POSITION_PRECEDING";
		if (nodeType == Node.DOCUMENT_POSITION_FOLLOWING)
			return "DOCUMENT_POSITION_FOLLOWING";
		if (nodeType == Node.DOCUMENT_POSITION_CONTAINS)
			return "DOCUMENT_POSITION_CONTAINS";
		if (nodeType == Node.DOCUMENT_POSITION_CONTAINED_BY)
			return "DOCUMENT_POSITION_CONTAINED_BY";
		if (nodeType == Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC)
			return "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC";

		throw new Exception("Unknown value : " + nodeType);
	}
}
