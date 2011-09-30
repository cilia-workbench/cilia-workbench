package fr.liglab.adele.cilia.workbench.designer.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;


public class XMLutil {

	public static boolean setRequiredAttribute(Node node, String attrName, Object object,
			String fieldName) throws MetadataException {
		return setAttributeInternal(true, null, node, attrName, object, fieldName);
	}
	
	public static boolean setOptionalAttribute(Node node, String attrName, Object object,
			String fieldName) throws MetadataException {
		return setOptionalAttribute(node, attrName, object, fieldName, null);
	}

	public static boolean setOptionalAttribute(Node node, String attrName, Object object,
			String fieldName, String defaultValue) throws MetadataException {
		return setAttributeInternal(false, defaultValue, node, attrName, object, fieldName);
	}
	
	private static boolean setAttributeInternal(boolean requiredAttribute, String defaultValue, Node node, String attrName, Object object,
			String fieldName) throws MetadataException {
		
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
				else
					if (defaultValue != null)
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

	
	public static String findAttributeValue(Node node, String attrName, String defaultValue)
			throws MetadataException {
		
		try {
			return findAttributeValue(node, attrName);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	private static String findAttributeValue(Node node, String attrName) throws MetadataException {
		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				String fullname = attr.getNodeName().toLowerCase();
				String name = computeName(fullname);
				String namespace = computeNamespace(fullname);
				String value = attr.getNodeValue();

				if (attrName.equals(name))
					return value;
			}
		}

		throw new MetadataException("Attribute " + attrName + " not found");
	}
	
	

	private static String computeNamespace(String fullname) {
		int location = fullname.indexOf(":");

		if (location == -1 || fullname.startsWith(":"))
			return "";
		else
			return fullname.substring(0, location);
	}

	private static String computeName(String fullname) {
		int location = fullname.indexOf(":");
		if (location == -1)
			return fullname;
		else
			return fullname.substring(location + 1);
	}

	
	public static Node findChild(Node node, String childName) {

		Node[] children = findChildren(node, childName);
		
		if (children.length == 0)
			return null;
		else
			return children[0];
	}
	
	public static Node[] findChildren(Node node, String childrenName) {
		
		List<Node> list = new ArrayList<Node>();
		
		NodeList childs = node.getChildNodes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);
				String localName = child.getNodeName();

				if (child.getNodeType() == Node.ELEMENT_NODE) {
					String name = computeName(localName);
					if (name.equals(childrenName))
						list.add(child);
				}
			}
		}
		
		return list.toArray(new Node[0]);
	}
	
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
