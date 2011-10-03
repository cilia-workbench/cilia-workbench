package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;


public abstract class Port {

	private String name;
	
	public Port(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
	}

	@Override
	public String toString() {
		return name;
	}
}
