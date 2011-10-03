package fr.liglab.adele.cilia.workbench.designer.parser.metadata;



import org.w3c.dom.Node;


public class Processor {

	private String name;
	private String classname;
	private String namespace;
	
	private String methodName;
	private String methodDataType;
	
	public Processor(Node node) throws MetadataException {
	
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "classname", this, "classname");
		XMLReflectionUtil.setOptionalAttribute(node, "namespace", this, "namespace");
		
		Node methodNode = XMLReflectionUtil.findChild(node, "method");
		if (methodNode == null)
			throw new MetadataException("method element not found");
		XMLReflectionUtil.setRequiredAttribute(methodNode, "name", this, "methodName");
		XMLReflectionUtil.setRequiredAttribute(methodNode, "data.type", this, "methodDataType");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
