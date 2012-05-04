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
package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Chain implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	private String id;
	private List<AdapterInstance> adapters = new ArrayList<AdapterInstance>();
	private List<MediatorInstance> mediators = new ArrayList<MediatorInstance>();
	private List<Binding> bindings = new ArrayList<Binding>();

	public Chain(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, "id", this, "id");

		Node rootAdapters = XMLHelpers.findChild(node, "adapters");
		if (rootAdapters != null) {
			Node[] ais = XMLHelpers.findChildren(rootAdapters, "adapter-instance");
			for (Node ai : ais)
				adapters.add(new AdapterInstance(ai));
		}

		Node rootMediators = XMLHelpers.findChild(node, "mediators");
		if (rootMediators != null) {
			Node[] mis = XMLHelpers.findChildren(rootMediators, "mediator-instance");
			for (Node mi : mis)
				mediators.add(new MediatorInstance(mi));
		}

		Node rootBindings = XMLHelpers.findChild(node, "bindings");
		if (rootBindings != null) {
			Node[] bis = XMLHelpers.findChildren(rootBindings, "binding");
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

	private ComponentInstance getComponent(String componentId) {
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

	public ComponentInstance[] getElements() {
		List<ComponentInstance> retval = new ArrayList<ComponentInstance>();
		for (AdapterInstance adapter : adapters)
			retval.add(adapter);
		for (MediatorInstance mediator : mediators)
			retval.add(mediator);
		return retval.toArray(new ComponentInstance[0]);
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
			} else {
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
			} else {
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

	public String isNewMediatorInstanceAllowed(String mediatorId, NameNamespaceID nn) {

		String message = null;
		if (Strings.isNullOrEmpty(mediatorId)) {
			message = "mediator id can't be empty";
		} else if (Strings.isNullOrEmpty(nn.getName())) {
			message = "mediator type can't be empty";
		} else {
			for (MediatorInstance m : mediators) {
				if (mediatorId.equalsIgnoreCase(m.getId()))
					message = "a mediator instance with id " + mediatorId + " already exists";
			}
		}

		return message;
	}

	public String isNewAdapterInstanceAllowed(String adapterId, NameNamespaceID nn) {
		String message = null;
		if (Strings.isNullOrEmpty(adapterId)) {
			message = "adapter id can't be empty";
		} else if (Strings.isNullOrEmpty(nn.getName())) {
			message = "adapter type can't be empty";
		} else {
			for (AdapterInstance a : adapters) {
				if (adapterId.equalsIgnoreCase(a.getId()))
					message = "an adapter instance with id " + adapterId + " already exists";
			}
		}

		return message;
	}

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
			String namespace = in.getNamespace();
			NameNamespaceID nn = new NameNamespaceID(type, namespace);
			Adapter ta = JarRepoService.getInstance().getAdapterForChain(nn);
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
			String namespace = out.getNamespace();
			NameNamespaceID nn = new NameNamespaceID(type, namespace);
			Adapter ta = JarRepoService.getInstance().getAdapterForChain(nn);
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

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id, "id");

		return CiliaFlag.generateTab(e1);
	}
}
