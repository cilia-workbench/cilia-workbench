package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;

public class Collector {

	private String name;
	private String classname;
	private String namespace;
	
	public Collector(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "name", this, "name");
		XMLutil.setRequiredAttribute(node, "classname", this, "classname");
		XMLutil.setOptionalAttribute(node, "namespace", this, "namespace");
	}

	@Override
	public String toString() {
		return name;
	}
}
