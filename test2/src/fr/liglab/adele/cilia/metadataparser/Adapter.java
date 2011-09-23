package fr.liglab.adele.cilia.metadataparser;

import org.w3c.dom.Node;

public class Adapter {

	private String name;
	private String pattern;
	
	private String collectorType;
	private String senderType;
	private String elementType;
	
	public Adapter(Node node) throws MetadataException {
		
		XMLutil.setAttribute(node, "name", this, "name");
		XMLutil.setAttribute(node, "pattern", this, "pattern");

		String subNodeName ;
		if (pattern.equals("in-only"))
			subNodeName = "collector";
		else if (pattern.equals("out-only"))
			subNodeName = "sender";
		else
			throw new MetadataException("Invalid pattern : " + pattern);
		
		Node subNode = XMLutil.findChild(node, subNodeName);
		if (subNode == null)
			throw new MetadataException(subNodeName + " element not found");
		XMLutil.setAttribute(subNode, "type", this, "elementType");
	}
}
