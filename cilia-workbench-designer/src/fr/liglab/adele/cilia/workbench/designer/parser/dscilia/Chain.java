package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

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

	public List<AdapterInstance> getAdapters() {
		return adapters;
	}
	
	public List<MediatorInstance> getMediators() {
		return mediators;
	}
	
	public List<Binding> getBindings() {
		return bindings;
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
	
	public String getId() {
		return id;
	}

	public Changeset[] merge(Chain newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		
		for (Iterator<AdapterInstance> itr = adapters.iterator(); itr.hasNext();) {
			AdapterInstance old = itr.next();
			String id = old.getId();
			AdapterInstance updated = pullAdapterInstance(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			}
			else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}
		
		for (Iterator<MediatorInstance> itr = mediators.iterator(); itr.hasNext();) {
			MediatorInstance old = itr.next();
			String id = old.getId();
			MediatorInstance updated = pullMediatorInstance(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			}
			else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}
		
		for (Iterator<Binding> itr = bindings.iterator(); itr.hasNext();) {
			Binding old = itr.next();
			String from = old.getSourceId();
			String to = old.getDestinationId();
			Binding updated = pullBinding(newInstance, from, to);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			}
			else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}
		
		for (AdapterInstance a : newInstance.getAdapters()) {
			adapters.add(a);
			retval.add(new Changeset(Operation.ADD, a));
		}
		
		for (MediatorInstance m : newInstance.getMediators()) {
			mediators.add(m);
			retval.add(new Changeset(Operation.ADD, m));
		}
		
		for (Binding b : newInstance.getBindings()) {
			bindings.add(b);
			retval.add(new Changeset(Operation.ADD, b));
		}
						
		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}

	private Binding pullBinding(Chain newInstance, String from, String to) {
		for (Iterator<Binding> itr = newInstance.getBindings().iterator(); itr.hasNext();) {
			Binding element = itr.next();
			if (element.getSourceId().equals(from) && element.getDestinationId().equals(to)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	private MediatorInstance pullMediatorInstance(Chain newInstance, String id) {
		for (Iterator<MediatorInstance> itr = newInstance.getMediators().iterator(); itr.hasNext();) {
			MediatorInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	private AdapterInstance pullAdapterInstance(Chain newInstance, String id) {
		for (Iterator<AdapterInstance> itr = newInstance.getAdapters().iterator(); itr.hasNext();) {
			AdapterInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
