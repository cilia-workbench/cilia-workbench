package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;

public class MediatorComponent {

	private String name;
	private String category;

	private String schedulerName;
	private String processorName;
	private String dispatcherName;
	private List<Port> ports = new ArrayList<Port>();

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

		Node portsNode = XMLutil.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLutil.findChildren(portsNode, "in-port");
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLutil.findChildren(portsNode, "out-port");
			for (Node outPort : outPorts)
				ports.add(new OutPort(outPort));
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
	public List<Port> getPorts() {
		return ports;
	}
}
