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
package fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IMediator;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Chain extends NameNamespace implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Mergeable {

	public static final String XML_NODE_NAME = "chain";

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	private List<AdapterComponent> adapters = new ArrayList<AdapterComponent>();
	private List<MediatorComponent> mediators = new ArrayList<MediatorComponent>();
	private List<Binding> bindings = new ArrayList<Binding>();

	public Chain(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");

		Node rootAdapters = XMLHelpers.findChild(node, "adapters");
		if (rootAdapters != null) {
			for (Node instance : XMLHelpers.findChildren(rootAdapters, AdapterInstance.XML_NODE_NAME))
				adapters.add(new AdapterInstance(instance, this));
		}

		Node rootMediators = XMLHelpers.findChild(node, "mediators");
		if (rootMediators != null) {
			for (Node instance : XMLHelpers.findChildren(rootMediators, MediatorInstance.XML_NODE_NAME))
				mediators.add(new MediatorInstance(instance, this));
			for (Node spec : XMLHelpers.findChildren(rootMediators, MediatorSpec.XML_NODE_NAME))
				mediators.add(new MediatorSpec(spec, this));
		}

		Node rootBindings = XMLHelpers.findChild(node, "bindings");
		if (rootBindings != null) {
			for (Node bi : XMLHelpers.findChildren(rootBindings, Binding.XML_NODE_NAME))
				bindings.add(new Binding(bi));
		}
	}

	public List<AdapterComponent> getAdapters() {
		return adapters;
	}

	public List<MediatorComponent> getMediators() {
		return mediators;
	}

	public List<Binding> getBindings() {
		return bindings;
	}

	public Binding getBinding(Component source, Component dest) {
		for (Binding b : bindings) {
			if (b.getSourceId().equals(source.getId()) && b.getDestinationId().equals(dest.getId()))
				return b;
		}

		return null;
	}

	public Component[] getDestinations(Component component) {
		return getDestinations(component.getId());
	}

	public Component[] getDestinations(String componentId) {
		List<Component> retval = new ArrayList<Component>();
		for (Binding binding : bindings) {
			String sourceId = binding.getSourceId();
			if (sourceId.equals(componentId)) {
				String destinationId = binding.getDestinationId();
				Component component = getComponent(destinationId);
				if (component != null)
					retval.add(component);
			}
		}
		return retval.toArray(new Component[0]);
	}

	public Component getComponent(String componentId) {
		for (AdapterComponent adapter : adapters)
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorComponent mediator : mediators)
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	public Component[] getComponents() {
		List<Component> retval = new ArrayList<Component>();
		for (AdapterComponent adapter : adapters)
			retval.add(adapter);
		for (MediatorComponent mediator : mediators)
			retval.add(mediator);
		return retval.toArray(new Component[0]);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();
		Chain newInstance = (Chain) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getAdapters(), adapters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getMediators(), mediators));
		retval.addAll(MergeUtil.mergeLists(newInstance.getBindings(), bindings));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public String isNewComponentAllowed(String elementId, NameNamespaceID nn) {

		String message = null;
		if (Strings.isNullOrEmpty(elementId)) {
			message = "element id can't be empty";
		} else if (Strings.isNullOrEmpty(nn.getName())) {
			message = "element type can't be empty";
		} else {
			if (getComponent(elementId) != null)
				message = "an element with id " + elementId + " already exists";
		}

		return message;
	}

	/**
	 * Finds the {@link IComponent} referenced by the chain component with id
	 * given into parameter. If the component can't be located, throws an
	 * exceprion containing an error message.
	 * 
	 * @param chainComponentID
	 * @return
	 * @throws CiliaException
	 */
	private IComponent getReferencedComponent(String chainComponentID) throws CiliaException {

		if (Strings.isNullOrEmpty(chainComponentID))
			throw new CiliaException("id is null or empty");

		Component component = getComponent(chainComponentID);
		if (component == null)
			throw new CiliaException("can't find component with id " + chainComponentID);

		NameNamespaceID referencedID = component.getReferencedTypeID();
		if (component instanceof AdapterInstance) {
			IAdapter adapterInstance = JarRepoService.getInstance().getAdapterForChain(referencedID);
			if (adapterInstance == null)
				throw new CiliaException("Adapter " + chainComponentID + " doesn't reference a valid adapter instance.");
			return adapterInstance;
		} else if (component instanceof MediatorSpec) {
			IMediator mediatorSpec = SpecRepoService.getInstance().getMediatorForChain(referencedID);
			if (mediatorSpec == null)
				throw new CiliaException("Mediator " + chainComponentID
						+ " doesn't reference a valid mediator instance.");
			return mediatorSpec;
		} else if (component instanceof MediatorInstance) {
			IMediator mediatorInstance = JarRepoService.getInstance().getMediatorForChain(referencedID);
			if (mediatorInstance == null)
				throw new CiliaException("Mediator " + chainComponentID
						+ " doesn't reference a valid mediator specification.");
			return mediatorInstance;
		} else {
			throw new CiliaException(component.getClass() + " handler not yet implemented here.");
		}
	}

	public String isNewBindingAllowed(String srcElem, String srcPort, String dstElem, String dstPort) {

		IComponent src;
		IComponent dst;
		if (srcElem.equalsIgnoreCase(dstElem))
			return "Source and destination can't be the same";

		try {
			src = getReferencedComponent(srcElem);
			dst = getReferencedComponent(dstElem);
		} catch (CiliaException e) {
			return e.getMessage();
		}

		if (src instanceof IAdapter) {
			IAdapter adapter = (IAdapter) src;
			if (adapter.isOutAdapter())
				return srcElem + " is an out-adapter. It can't be a binding source.";
		}

		if (dst instanceof IAdapter) {
			IAdapter adapter = (IAdapter) dst;
			if (adapter.isInAdapter())
				return dstElem + " is an in-adapter. It can't be a binding destination.";
		}

		return null;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		List<CiliaFlag> list = IdentifiableUtils.getErrorsNonUniqueId(this, getComponents());

		for (CiliaFlag flag : tab)
			list.add(flag);

		for (Component c : getComponents()) {
			try {
				getReferencedComponent(c.getId());
			} catch (CiliaException e) {
				list.add(new CiliaError(e.getMessage(), this));
			}
		}

		for (Binding b : getBindings()) {

			// source
			try {
				IComponent src = getReferencedComponent(b.getSourceId());
				if (src instanceof IAdapter) {
					if (((IAdapter) src).isOutAdapter())
						list.add(new CiliaError("Binding " + b + " has its source connected to an out adapter", this));
					else if (!Strings.isNullOrEmpty(b.getSourcePort()))
						list.add(new CiliaError("Binding " + b
								+ " reference an in port but it's linked to an in adapter", this));
				} else if (src instanceof IMediator) {
					if (!Strings.isNullOrEmpty(b.getSourcePort())) {
						IMediator mediator = (IMediator) src;

						boolean found = false;
						for (IGenericPort p : mediator.getOutPorts())
							if (p.getName().equalsIgnoreCase(b.getSourcePort()))
								found = true;
						if (!found)
							list.add(new CiliaError("Binding " + b + " doesn't reference a valid source port", this));
					}
				} else {
					throw new RuntimeException(src.getClass() + "unknown");
				}
			} catch (CiliaException e) {
				list.add(new CiliaError("Binding " + b + " doesn't have a valid source", this));
			}

			// destination
			try {
				IComponent dst = getReferencedComponent(b.getDestinationId());
				if (dst instanceof IAdapter) {
					if (((IAdapter) dst).isInAdapter())
						list.add(new CiliaError("Binding " + b + " has its dstination connected to an in adapter", this));
					else if (!Strings.isNullOrEmpty(b.getDestinationPort()))
						list.add(new CiliaError("Binding " + b
								+ " reference an out port but it's linked to an out adapter", this));
				} else if (dst instanceof IMediator) {
					if (!Strings.isNullOrEmpty(b.getDestinationPort())) {
						IMediator mediator = (IMediator) dst;

						boolean found = false;
						for (IGenericPort p : mediator.getInPorts())
							if (p.getName().equalsIgnoreCase(b.getDestinationPort()))
								found = true;
						if (!found)
							list.add(new CiliaError("Binding " + b + " doesn't reference a valid destination port",
									this));
					}
				} else {
					throw new RuntimeException(dst.getClass() + "unknown");
				}
			} catch (CiliaException e) {
				list.add(new CiliaError("Binding " + b + " doesn't have a valid destination", this));
			}
		}

		return CiliaFlag.generateTab(list);
	}
}
