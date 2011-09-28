package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Sender;

public class Binding {

	private String from;
	private String to;
	
	public Binding(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "from", this, "from");
		XMLutil.setRequiredAttribute(node, "to", this, "to");
	}
}
