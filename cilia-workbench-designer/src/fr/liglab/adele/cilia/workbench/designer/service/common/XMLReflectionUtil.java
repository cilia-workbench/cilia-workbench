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
package fr.liglab.adele.cilia.workbench.designer.service.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.liglab.adele.cilia.workbench.common.misc.XMLUtil;

/**
 * A set of static methods used to parse XML files and to inject fields values
 * in an object using reflexion.
 * 
 * @author Etienne Gandrille
 */
public class XMLReflectionUtil {

	/**
	 * Sets an attribute, and sends an exception if the attribute can't be found
	 * or set.
	 * 
	 * @param node
	 *            an XML node, used to find the attribute and the associated
	 *            value.
	 * @param attrName
	 *            the name of the attribute in the XML node.
	 * @param object
	 *            the object on which the value should be injected.
	 * @param fieldName
	 *            the object field name, for value injection.
	 * @throws MetadataException
	 *             error.
	 */
	public static void setRequiredAttribute(Node node, String attrName, Object object, String fieldName)
			throws MetadataException {
		setAttributeInternal(true, null, node, attrName, object, fieldName);
	}

	/**
	 * Sets an optional attribute.
	 * 
	 * @param node
	 *            an XML node, used to find the attribute and the associated
	 *            value.
	 * @param attrName
	 *            the name of the attribute in the XML node.
	 * @param object
	 *            the object on which the value should be injected.
	 * @param fieldName
	 *            the object field name, for value injection.
	 * @return true, if successful
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public static boolean setOptionalAttribute(Node node, String attrName, Object object, String fieldName)
			throws MetadataException {
		return setOptionalAttribute(node, attrName, object, fieldName, null);
	}

	/**
	 * Sets an optional attribute.
	 * 
	 * @param node
	 *            an XML node, used to find the attribute and the associated
	 *            value.
	 * @param attrName
	 *            the name of the attribute in the XML node.
	 * @param object
	 *            the object on which the value should be injected.
	 * @param fieldName
	 *            the object field name, for value injection.
	 * @param defaultValue
	 *            a default value, injected if no value found in the XML node.
	 * @return true, if successful
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public static boolean setOptionalAttribute(Node node, String attrName, Object object, String fieldName,
			String defaultValue) throws MetadataException {
		return setAttributeInternal(false, defaultValue, node, attrName, object, fieldName);
	}

	/**
	 * The internal method, for setting an attribute.
	 * 
	 * @param requiredAttribute
	 *            tells if the attribute is required. If <code>true</code>,
	 *            sends an exception if the attribute can't be found in the XML
	 *            file.
	 * @param defaultValue
	 *            the default value, used if it's not null AND if
	 *            requiredAttribute is <code>false</code>.
	 * @param node
	 *            an XML node, used to find the attribute and the associated
	 *            value.
	 * @param attrName
	 *            the name of the attribute in the XML node.
	 * @param object
	 *            the object on which the value should be injected.
	 * @param fieldName
	 *            the object field name, for value injection.
	 * @return true, if successful
	 * @throws MetadataException
	 *             the metadata exception
	 */
	private static boolean setAttributeInternal(boolean requiredAttribute, String defaultValue, Node node,
			String attrName, Object object, String fieldName) throws MetadataException {

		Exception exception = null;
		boolean retval = true;
		try {
			Field field;
			try {
				field = object.getClass().getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				field = object.getClass().getSuperclass().getDeclaredField(fieldName);
			}
			String value = null;

			try {
				value = findAttributeValue(node, attrName);
			} catch (MetadataException e) {
				retval = false;
				if (requiredAttribute == true)
					throw new MetadataException(e);
				else if (defaultValue != null)
					value = defaultValue;
			}

			field.setAccessible(true);
			field.set(object, value);

		} catch (SecurityException e) {
			exception = e;
		} catch (NoSuchFieldException e) {
			exception = e;
		} catch (IllegalArgumentException e) {
			exception = e;
		} catch (IllegalAccessException e) {
			exception = e;
		}

		// If there's an error
		if (exception != null)
			throw new MetadataException("", exception);

		// Success
		return retval;
	}

	/**
	 * Finds an attribute value in a node.
	 * 
	 * @param node
	 *            an XML node, used to find the attribute and the associated
	 *            value.
	 * @param attrName
	 *            the name of the attribute in the XML node.
	 * @param defaultValue
	 *            the default value, returns if the attribute value can't be
	 *            found.
	 * @return the attribute value
	 */
	public static String findAttributeValue(Node node, String attrName, String defaultValue) {

		try {
			return findAttributeValue(node, attrName);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Find attribute value.
	 * 
	 * @param node
	 *            the node
	 * @param attrName
	 *            the attr name
	 * @return the string
	 * @throws MetadataException
	 *             the metadata exception
	 */
	private static String findAttributeValue(Node node, String attrName) throws MetadataException {
		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				String fullname = attr.getNodeName().toLowerCase();
				String name = XMLUtil.computeName(fullname);
				String namespace = XMLUtil.computeNamespace(fullname);
				String value = attr.getNodeValue();

				if (attrName.equals(name))
					return value;
			}
		}

		throw new MetadataException("Attribute " + attrName + " not found");
	}

	/**
	 * Finds a child node.
	 * 
	 * @param node
	 *            the root node
	 * @param childName
	 *            the child node name
	 * @return the child node
	 */
	public static Node findChild(Node node, String childName) {

		Node[] children = findChildren(node, childName);

		if (children.length == 0)
			return null;
		else
			return children[0];
	}

	/**
	 * Finds all the children nodes.
	 * 
	 * @param node
	 *            the root node
	 * @param childrenName
	 *            the children name
	 * @return the children list
	 */
	public static Node[] findChildren(Node node, String childrenName) {

		List<Node> list = new ArrayList<Node>();

		NodeList childs = node.getChildNodes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);
				String localName = child.getNodeName();

				if (child.getNodeType() == Node.ELEMENT_NODE) {
					String name = XMLUtil.computeName(localName);
					if (name.equals(childrenName))
						list.add(child);
				}
			}
		}

		return list.toArray(new Node[0]);
	}

	/**
	 * Converts a node type to its associated string. This helper method can be
	 * useful for debugging.
	 * 
	 * @param nodeType
	 *            the node type, as a constant
	 * @return the node type, as a string
	 * @throws Exception
	 *             if node type can't be found.
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
