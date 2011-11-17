/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Adapter;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;

/**
 * Represents a Cilia chain.
 * 
 * Potential BUG: spec is not clear whereas a mediator and an adapter can have
 * the same id.
 * 
 * @author Etienne Gandrille
 */
public class Chain {

	/** The chain id. */
	private String id;

	/** The adapters, contained by the chain. */
	private List<AdapterInstance> adapters = new ArrayList<AdapterInstance>();

	/** The mediators, contained by the chain. */
	private List<MediatorInstance> mediators = new ArrayList<MediatorInstance>();

	/** The bindings inside the chain. */
	private List<Binding> bindings = new ArrayList<Binding>();

	/** The XML DOM node. */
	private Node node;

	/**
	 * Instantiates a new chain, using reflection on the DOM node.
	 * 
	 * @param node
	 *            the DOM node
	 * @throws MetadataException
	 *             XML parsing error, or reflexion error.
	 */
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

	/**
	 * Gets the adapters.
	 * 
	 * @return the adapters
	 */
	public List<AdapterInstance> getAdapters() {
		return adapters;
	}

	/**
	 * Gets the mediators.
	 * 
	 * @return the mediators
	 */
	public List<MediatorInstance> getMediators() {
		return mediators;
	}

	/**
	 * Gets the bindings.
	 * 
	 * @return the bindings
	 */
	public List<Binding> getBindings() {
		return bindings;
	}

	/**
	 * Gets the destinations an adapter can join by its bindings.
	 * 
	 * @param adapter
	 *            the adapter
	 * @return the destinations
	 */
	public Object[] getDestinations(AdapterInstance adapter) {
		return getDestinations(adapter.getId());
	}

	/**
	 * Gets the destinations a mediator can join by its bindings.
	 * 
	 * @param mediator
	 *            the mediator
	 * @return the destinations
	 */
	public Object[] getDestinations(MediatorInstance mediator) {
		return getDestinations(mediator.getId());
	}

	/**
	 * Gets the destinations a component can join by its bindings.
	 * 
	 * @param componentId
	 *            the component id
	 * @return the destinations
	 */
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

	/**
	 * Gets the component using its id.
	 * 
	 * @param componentId
	 *            the component id
	 * @return the component
	 */
	private ComponentInstance getComponent(String componentId) {
		for (AdapterInstance adapter : adapters)
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorInstance mediator : mediators)
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

	/**
	 * Gets all the components (mediators and adapters).
	 * 
	 * @return the components
	 */
	public ComponentInstance[] getComponents() {
		List<ComponentInstance> retval = new ArrayList<ComponentInstance>();
		for (AdapterInstance adapter : adapters)
			retval.add(adapter);
		for (MediatorInstance mediator : mediators)
			retval.add(mediator);
		return retval.toArray(new ComponentInstance[0]);
	}

	/**
	 * Gets the chain id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Merge another {@link Chain} into the current one. Differences between the
	 * argument and the current object are injected into the current object.
	 * 
	 * @param newInstance
	 *            an 'up-to-date' object
	 * @return a list of {@link Changeset}, which can be empty.
	 */
	protected Changeset[] merge(Chain newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<AdapterInstance> itr = adapters.iterator(); itr.hasNext();) {
			AdapterInstance old = itr.next();
			String id = old.getId();
			AdapterInstance updated = MergeUtil.pullAdapterInstance(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (Iterator<MediatorInstance> itr = mediators.iterator(); itr.hasNext();) {
			MediatorInstance old = itr.next();
			String id = old.getId();
			MediatorInstance updated = MergeUtil.pullMediatorInstance(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (Iterator<Binding> itr = bindings.iterator(); itr.hasNext();) {
			Binding old = itr.next();
			String from = old.getSourceId();
			String to = old.getDestinationId();
			Binding updated = MergeUtil.pullBinding(newInstance, from, to);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
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

	/**
	 * Checks if is new mediator instance allowed. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param mediatorId
	 *            the mediator id
	 * @param mediatorType
	 *            the mediator type
	 * @return null if no error detected, an error message (including "")
	 *         otherwise.
	 */
	public String isNewMediatorInstanceAllowed(String mediatorId, String mediatorType) {
		return isNewComponentInstanceAllowed(mediatorId, mediatorType);
	}

	/**
	 * Checks if is new adapter instance allowed. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param adapterId
	 *            the adapter id
	 * @param adapterType
	 *            the adapter type
	 * @return null if no error detected, an error message (including "")
	 *         otherwise.
	 */
	public String isNewAdapterInstanceAllowed(String adapterId, String adapterType) {
		return isNewComponentInstanceAllowed(adapterId, adapterType);
	}

	/**
	 * Checks if is new component instance is allowed. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param componentId
	 *            the component id
	 * @param componentType
	 *            the component type
	 * @return null if no error detected, an error message (including "")
	 *         otherwise.
	 */
	private String isNewComponentInstanceAllowed(String componentId, String componentType) {
		String message = null;
		if (Strings.isNullOrEmpty(componentId)) {
			message = "component id can't be empty";
		} else if (Strings.isNullOrEmpty(componentType)) {
			message = "component type can't be empty";
		} else {
			for (AdapterInstance a : adapters) {
				if (componentId.equalsIgnoreCase(a.getId()))
					message = "a componant (adapter) instance with id " + a.getId() + " already exists";
			}
			for (MediatorInstance m : mediators) {
				if (componentId.equalsIgnoreCase(m.getId()))
					message = "a component (mediator) instance with id " + m.getId() + " already exists";
			}
		}

		return message;
	}

	/**
	 * Checks if is new binding allowed. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param srcElem
	 *            the src elem
	 * @param srcPort
	 *            the src port
	 * @param dstElem
	 *            the dst elem
	 * @param dstPort
	 *            the dst port
	 * @return null if no error detected, an error message (including "")
	 *         otherwise.
	 */
	public String isNewBindingAllowed(String srcElem, String srcPort, String dstElem, String dstPort) {
		if (Strings.isNullOrEmpty(srcElem))
			return "Source element can't be empty";
		if (Strings.isNullOrEmpty(dstElem))
			return "Destination element can't be empty";
		if (srcElem.equalsIgnoreCase(dstElem))
			return "Source and destination can't be the same";

		ComponentInstance src = getComponent(srcElem);
		ComponentInstance dst = getComponent(dstElem);
		if (src == null)
			return "Can't find " + srcElem + " in chain " + getId();
		if (dst == null)
			return "Can't find " + dstElem + " in chain " + getId();

		if (src instanceof AdapterInstance) {
			AdapterInstance in = (AdapterInstance) src;
			String type = in.getType();
			Adapter ta = JarRepoService.getInstance().getAdapter(type);
			if (ta != null) {
				if (ta.getPattern().equals(Adapter.IN_PATTERN))
					return src.id + " is an in-adapter. It can't be a binding source.";
			} else {
				for (Binding b : bindings)
					if (b.getDestinationId().equals(src.id))
						return src.id + " is already used as an in-adapter";
			}
		}

		if (dst instanceof AdapterInstance) {
			AdapterInstance out = (AdapterInstance) dst;
			String type = out.getType();
			Adapter ta = JarRepoService.getInstance().getAdapter(type);
			if (ta != null) {
				if (ta.getPattern().equals(Adapter.OUT_PATTERN))
					return dst.id + " is an out-adapter. It can't be a binding destination.";
			} else {
				for (Binding b : bindings)
					if (b.getSourceId().equals(dst.id))
						return dst.id + " is already used as an out-adapter";
			}
		}

		return null;
	}
}
