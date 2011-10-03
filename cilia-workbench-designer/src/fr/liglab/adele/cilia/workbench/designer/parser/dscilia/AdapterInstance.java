package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

public class AdapterInstance {

	private String id;
	private String type;
	
	public AdapterInstance(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "id", this, "id");
		XMLReflectionUtil.setRequiredAttribute(node, "type", this, "type");
	}
	
	public String getId() {
		return id;
	}

	public Changeset[] merge(AdapterInstance newInstance) {
		if (type.equals(newInstance.type))
			return new Changeset[0];
		else {
			type = newInstance.type;
			Changeset[] retval = new Changeset[1];
			retval[0]=new Changeset(Operation.UPDATE, this);
			return retval;
		}
	}
}
