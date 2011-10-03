package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;


public class Collector {

	private String name;
	private String classname;
	private String namespace;
	
	public Collector(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "classname", this, "classname");
		XMLReflectionUtil.setOptionalAttribute(node, "namespace", this, "namespace");
	}

	@Override
	public String toString() {
		return name;
	}
}
