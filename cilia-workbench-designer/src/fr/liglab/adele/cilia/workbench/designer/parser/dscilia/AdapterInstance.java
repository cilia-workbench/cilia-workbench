package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class AdapterInstance {

	private String id;
	private String type;
	
	public AdapterInstance(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "id", this, "id");
		XMLutil.setRequiredAttribute(node, "type", this, "type");
	}
	
	public String getId() {
		return id;
	}
}
