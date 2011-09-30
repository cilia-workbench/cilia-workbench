package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;

public abstract class Port {

	private String name;
	
	public Port(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "name", this, "name");
	}

	@Override
	public String toString() {
		return name;
	}
}
