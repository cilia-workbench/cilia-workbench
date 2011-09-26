package fr.liglab.adele.cilia.workbench.designer.metadataparser;

import org.w3c.dom.Node;

public class Dispatcher {

	private String name;
	private String classname;
	private String namespace;
	
	public Dispatcher(Node node) throws MetadataException {
		XMLutil.setAttribute(node, "name", this, "name");
		XMLutil.setAttribute(node, "classname", this, "classname");
		XMLutil.setAttribute(null, node, "namespace", this, "namespace");
	}

	@Override
	public String toString() {
		return name;
	}
}
