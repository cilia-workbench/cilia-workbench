package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
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
		return StringUtil.getBeforeSeparatorOrAll(from);
	}
	
	public String getDestinationId() {
		return StringUtil.getBeforeSeparatorOrAll(to);
	}
	
	public Changeset[] merge(Binding newInstance) {
		return new Changeset[0];
	}
}
