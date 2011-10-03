package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;

public abstract class ComponentInstance {

	protected String id;
	protected String type;

	public ComponentInstance(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "id", this, "id");
		XMLReflectionUtil.setRequiredAttribute(node, "type", this, "type");
	}
	
	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}
}
