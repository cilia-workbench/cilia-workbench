package fr.liglab.adele.cilia.workbench.designer.metadataparser;

import org.w3c.dom.Node;

public class Scheduler {

	private String name;
	private String classname;
	private String namespace;
	
	public Scheduler(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "name", this, "name");
		XMLutil.setRequiredAttribute(node, "classname", this, "classname");
		XMLutil.setOptionalAttribute(node, "namespace", this, "namespace");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
