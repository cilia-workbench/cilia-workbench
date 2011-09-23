package fr.liglab.adele.cilia.metadataparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class IPojo {

	private List<MediatorComponent> mediatorComponents = new ArrayList<MediatorComponent>();
	
	private List<Processor> processors = new ArrayList<Processor>();
	private List<Scheduler> schedulers = new ArrayList<Scheduler>();
	private List<Dispatcher> dispatchers = new ArrayList<Dispatcher>();
	private List<Collector> collectors = new ArrayList<Collector>();
	private List<Sender> senders = new ArrayList<Sender>();
	private List<Adapter> adapters = new ArrayList<Adapter>();

	public IPojo(Node node) throws Exception {
		
		NodeList childs = node.getChildNodes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);
				String nodeName = child.getNodeName().toLowerCase();

				if (child.getNodeType() == Node.ELEMENT_NODE) {
					
					if (nodeName.equals("processor"))
						processors.add(new Processor(child));
					else if (nodeName.equals("scheduler"))
						schedulers.add(new Scheduler(child));
					else if (nodeName.equals("dispatcher"))
						dispatchers.add(new Dispatcher(child));
					else if (nodeName.equals("collector"))
						collectors.add(new Collector(child));
					else if (nodeName.equals("sender"))
						senders.add(new Sender(child));
					else if (nodeName.equals("adapter"))
						adapters.add(new Adapter(child));
					else if (nodeName.equals("mediator-component"))
						mediatorComponents.add(new MediatorComponent(child));
				}
			}
		}
	}
}
