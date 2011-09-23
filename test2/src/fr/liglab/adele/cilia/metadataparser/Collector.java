package fr.liglab.adele.cilia.metadataparser;

import org.w3c.dom.Node;

public class Collector {

	private String name;
	private String classname;
	private String namespace;
	
	public Collector(Node node) throws MetadataException {
		XMLutil.setAttribute(node, "name", this, "name");
		XMLutil.setAttribute(node, "classname", this, "classname");
		XMLutil.setAttribute(null, node, "namespace", this, "namespace");
	}

}
