package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class Chain {

	private String id;
	
	public Chain(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "id", this, "id");
	}
	
	@Override
	public String toString() {
		return id;
	}
}
