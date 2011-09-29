package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;

public class Binding {

	private String from;
	private String to;
	
	public Binding(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "from", this, "from");
		XMLutil.setRequiredAttribute(node, "to", this, "to");
	}

	public String getSourceId() {
		return getId(from);
	}
	
	public String getDestinationId() {
		return getId(to);
	}
	
	private String getId(String name) {
		int index = name.indexOf(":");
		if (index == -1)
			return name;
		String retval = name.substring(0, index);
		return retval;
	}

	public Changeset[] merge(Binding newInstance) {
		return new Changeset[0];
	}
}
