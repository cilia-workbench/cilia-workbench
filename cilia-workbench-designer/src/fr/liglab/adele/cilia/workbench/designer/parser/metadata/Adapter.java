package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;


public class Adapter {

	private String name;
	private String pattern;
	
	private String collectorType;
	private String senderType;
	private String elementType;
	
	public Adapter(Node node) throws MetadataException {
		
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "pattern", this, "pattern");

		String subNodeName ;
		if (pattern.equals("in-only"))
			subNodeName = "collector";
		else if (pattern.equals("out-only"))
			subNodeName = "sender";
		else
			throw new MetadataException("Invalid pattern : " + pattern);
		
		Node subNode = XMLReflectionUtil.findChild(node, subNodeName);
		if (subNode == null)
			throw new MetadataException(subNodeName + " element not found");
		XMLReflectionUtil.setRequiredAttribute(subNode, "type", this, "elementType");
	}
	
	public String getPattern() {
		return pattern;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
