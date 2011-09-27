package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;

public class MediatorComponent {

	private String name;
	private String category;
	
	private String schedulerName;
	private String processorName;
	private String dispatcherName;
	
	
	public MediatorComponent(Node node) throws MetadataException {
		
		XMLutil.setRequiredAttribute(node, "name", this, "name");
		XMLutil.setRequiredAttribute(node, "category", this, "category");
	
		Node schedulerNode = XMLutil.findChild(node, "scheduler");
		if (schedulerNode == null)
			throw new MetadataException("scheduler element not found");
		XMLutil.setRequiredAttribute(schedulerNode, "name", this, "schedulerName");
		
		Node processorNode = XMLutil.findChild(node, "processor");
		if (processorNode == null)
			throw new MetadataException("processor element not found");
		XMLutil.setRequiredAttribute(processorNode, "name", this, "processorName");
		
		Node dispatcherNode = XMLutil.findChild(node, "dispatcher");
		if (dispatcherNode == null)
			throw new MetadataException("dispatcher element not found");
		XMLutil.setRequiredAttribute(dispatcherNode, "name", this, "dispatcherName");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
