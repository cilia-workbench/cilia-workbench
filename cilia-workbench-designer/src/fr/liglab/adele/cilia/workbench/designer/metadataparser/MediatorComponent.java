package fr.liglab.adele.cilia.workbench.designer.metadataparser;

import org.w3c.dom.Node;

public class MediatorComponent {

	private String name;
	private String category;
	
	private String internal_schedulerName;
	private String internal_processorName;
	private String internal_dispatcherName;
	
	
	public MediatorComponent(Node node) throws MetadataException {
		
		XMLutil.setAttribute(node, "name", this, "name");
		XMLutil.setAttribute(node, "category", this, "category");
	
		Node schedulerNode = XMLutil.findChild(node, "scheduler");
		if (schedulerNode == null)
			throw new MetadataException("scheduler element not found");
		XMLutil.setAttribute(schedulerNode, "name", this, "internal_schedulerName");
		
		Node processorNode = XMLutil.findChild(node, "processor");
		if (processorNode == null)
			throw new MetadataException("processor element not found");
		XMLutil.setAttribute(processorNode, "name", this, "internal_processorName");
		
		Node dispatcherNode = XMLutil.findChild(node, "dispatcher");
		if (dispatcherNode == null)
			throw new MetadataException("dispatcher element not found");
		XMLutil.setAttribute(dispatcherNode, "name", this, "internal_dispatcherName");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
