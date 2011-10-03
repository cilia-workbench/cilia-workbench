package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;


public class MediatorComponent {

	private String name;
	private String category;

	private String schedulerName;
	private String processorName;
	private String dispatcherName;
	private List<Port> ports = new ArrayList<Port>();

	public MediatorComponent(Node node) throws MetadataException {

		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "category", this, "category");

		Node schedulerNode = XMLReflectionUtil.findChild(node, "scheduler");
		if (schedulerNode == null)
			throw new MetadataException("scheduler element not found");
		XMLReflectionUtil.setRequiredAttribute(schedulerNode, "name", this, "schedulerName");

		Node processorNode = XMLReflectionUtil.findChild(node, "processor");
		if (processorNode == null)
			throw new MetadataException("processor element not found");
		XMLReflectionUtil.setRequiredAttribute(processorNode, "name", this, "processorName");

		Node dispatcherNode = XMLReflectionUtil.findChild(node, "dispatcher");
		if (dispatcherNode == null)
			throw new MetadataException("dispatcher element not found");
		XMLReflectionUtil.setRequiredAttribute(dispatcherNode, "name", this, "dispatcherName");

		Node portsNode = XMLReflectionUtil.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLReflectionUtil.findChildren(portsNode, "in-port");
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLReflectionUtil.findChildren(portsNode, "out-port");
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
	
	public String getName() {
		return name;
	}
}
