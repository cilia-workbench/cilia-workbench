package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class Chain {

	private String id;
	private List<AdapterInstance> adapters = new ArrayList<AdapterInstance>();
	private List<MediatorInstance> mediators = new ArrayList<MediatorInstance>();
	private List<Binding> bindings = new ArrayList<Binding>();

	public Chain(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "id", this, "id");

		Node rootAdapters = XMLutil.findChild(node, "adapters");
		if (rootAdapters != null) {
			Node[] ais = XMLutil.findChildren(rootAdapters, "adapter-instance");
			for (Node ai : ais)
				adapters.add(new AdapterInstance(ai));
		}

		Node rootMediators = XMLutil.findChild(node, "mediators");
		if (rootMediators != null) {
			Node[] mis = XMLutil.findChildren(rootMediators, "mediator-instance");
			for (Node mi : mis)
				mediators.add(new MediatorInstance(mi));
		}

		Node rootBindings = XMLutil.findChild(node, "bindings");
		if (rootBindings != null) {
			Node[] bis = XMLutil.findChildren(rootBindings, "binding");
			for (Node bi : bis)
				bindings.add(new Binding(bi));
		}
	}

	public Object[] getDestinations(AdapterInstance adapter) {
		return getDestinations(adapter.getId());
	}

	public Object[] getDestinations(MediatorInstance mediator) {
		return getDestinations(mediator.getId());
	}

	public Object[] getDestinations(String componentId) {
		List<Object> retval = new ArrayList<Object>();
		for (Binding binding : bindings) {
			String sourceId = binding.getSourceId();
			if (sourceId.equals(componentId)) {
				String destinationId = binding.getDestinationId();
				Object component = getComponent(destinationId);
				if (component != null)
					retval.add(component);
			}
		}
		return retval.toArray();
	}

	private Object getComponent(String componentId) {
		for (AdapterInstance adapter : adapters)
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorInstance mediator : mediators)
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	@Override
	public String toString() {
		return id;
	}

	public Object[] getElements() {
		List<Object> retval = new ArrayList<Object>();
		for (AdapterInstance adapter : adapters)
			retval.add(adapter);
		for (MediatorInstance mediator : mediators)
			retval.add(mediator);
		return retval.toArray();
	}
}
