package fr.liglab.adele.cilia.metadataparser;



import org.w3c.dom.Node;

public class Processor {

	private String name;
	private String classname;
	private String namespace;
	
	private String methodName;
	private String methodDataType;
	
	public Processor(Node node) throws MetadataException {
	
		XMLutil.setAttribute(node, "name", this, "name");
		XMLutil.setAttribute(node, "classname", this, "classname");
		XMLutil.setAttribute(null, node, "namespace", this, "namespace");
		
		Node methodNode = XMLutil.findChild(node, "method");
		if (methodNode == null)
			throw new MetadataException("method element not found");
		XMLutil.setAttribute(methodNode, "name", this, "methodName");
		XMLutil.setAttribute(methodNode, "data.type", this, "methodDataType");
	}
}
