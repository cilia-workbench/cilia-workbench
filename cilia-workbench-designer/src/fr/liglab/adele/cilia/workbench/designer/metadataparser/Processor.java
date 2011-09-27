package fr.liglab.adele.cilia.workbench.designer.metadataparser;



import org.w3c.dom.Node;

public class Processor {

	private String name;
	private String classname;
	private String namespace;
	
	private String methodName;
	private String methodDataType;
	
	public Processor(Node node) throws MetadataException {
	
		XMLutil.setRequiredAttribute(node, "name", this, "name");
		XMLutil.setRequiredAttribute(node, "classname", this, "classname");
		XMLutil.setOptionalAttribute(node, "namespace", this, "namespace");
		
		Node methodNode = XMLutil.findChild(node, "method");
		if (methodNode == null)
			throw new MetadataException("method element not found");
		XMLutil.setRequiredAttribute(methodNode, "name", this, "methodName");
		XMLutil.setRequiredAttribute(methodNode, "data.type", this, "methodDataType");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
