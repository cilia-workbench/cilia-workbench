package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;

public class Binding {

	private String from;
	private String to;

	public Binding(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "from", this, "from");
		XMLReflectionUtil.setRequiredAttribute(node, "to", this, "to");
	}

	public String getSource() {
		return from;
	}

	public String getDestination() {
		return to;
	}

	public String getSourceId() {
		return StringUtil.getBeforeSeparatorOrAll(from);
	}

	public String getDestinationId() {
		return StringUtil.getBeforeSeparatorOrAll(to);
	}

	public String getSourcePort() {
		return StringUtil.getAfterSeparatorOrNothing(from);
	}

	public String getDestinationPort() {
		return StringUtil.getAfterSeparatorOrNothing(to);
	}

	public Changeset[] merge(Binding newInstance) {
		return new Changeset[0];
	}
}
