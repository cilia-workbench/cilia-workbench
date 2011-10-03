package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

public class Chain {

	private String id;
	private List<AdapterInstance> adapters = new ArrayList<AdapterInstance>();
	private List<MediatorInstance> mediators = new ArrayList<MediatorInstance>();
	private List<Binding> bindings = new ArrayList<Binding>();
	private Node node;

	public Chain(Node node) throws MetadataException {
		this.node = node;
		XMLReflectionUtil.setRequiredAttribute(node, "id", this, "id");

		Node rootAdapters = XMLReflectionUtil.findChild(node, "adapters");
		if (rootAdapters != null) {
			Node[] ais = XMLReflectionUtil.findChildren(rootAdapters, "adapter-instance");
			for (Node ai : ais)
				adapters.add(new AdapterInstance(ai));
		}

		Node rootMediators = XMLReflectionUtil.findChild(node, "mediators");
		if (rootMediators != null) {
			Node[] mis = XMLReflectionUtil.findChildren(rootMediators, "mediator-instance");
			for (Node mi : mis)
				mediators.add(new MediatorInstance(mi));
		}

		Node rootBindings = XMLReflectionUtil.findChild(node, "bindings");
		if (rootBindings != null) {
			Node[] bis = XMLReflectionUtil.findChildren(rootBindings, "binding");
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
			AdapterInstance updated = PullElementUtil.pullAdapterInstance(newInstance, id);
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
			MediatorInstance updated = PullElementUtil.pullMediatorInstance(newInstance, id);
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
			Binding updated = PullElementUtil.pullBinding(newInstance, from, to);
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
	
	public String isNewMediatorInstanceAllowed(String mediatorId, String mediatorType) {
		
		String message = null;
		if (Strings.isNullOrEmpty(mediatorId)) {
			message = "mediator id can't be empty";
		} else if (Strings.isNullOrEmpty(mediatorType)) {
			message = "mediator type can't be empty";
		} else {
			for (MediatorInstance m : mediators) {
				if (mediatorId.equalsIgnoreCase(m.getId()))
					message = "a mediator instance with id " + mediatorId + " already exists";
			}
		}
		
		return message;
	}

	public String isNewAdapterInstanceAllowed(String adapterId, String adapterType) {
		String message = null;
		if (Strings.isNullOrEmpty(adapterId)) {
			message = "adapter id can't be empty";
		} else if (Strings.isNullOrEmpty(adapterType)) {
			message = "adapter type can't be empty";
		} else {
			for (AdapterInstance a : adapters) {
				if (adapterId.equalsIgnoreCase(a.getId()))
					message = "an adapter instance with id " + adapterId + " already exists";
			}
		}
		
		return message;
	}
}
