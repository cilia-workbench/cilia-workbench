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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;
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

	public static final String XML_ROOT_MEDIATORS_NAME = "mediators";
	public static final String XML_ROOT_ADAPTERS_NAME = "adapters";
	public static final String XML_ROOT_BINDINGS_NAME = "bindings";

	private List<AdapterRef> adapters = new ArrayList<AdapterRef>();
	private List<MediatorRef> mediators = new ArrayList<MediatorRef>();
	private List<Binding> bindings = new ArrayList<Binding>();

	public Chain(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");

		Node rootAdapters = XMLHelpers.findChild(node, XML_ROOT_ADAPTERS_NAME);
		if (rootAdapters != null) {
			for (Node instance : XMLHelpers.findChildren(rootAdapters, AdapterInstanceRef.XML_NODE_NAME))
				adapters.add(new AdapterInstanceRef(instance, getId()));
		}

		Node rootMediators = XMLHelpers.findChild(node, XML_ROOT_MEDIATORS_NAME);
		if (rootMediators != null) {
			for (Node instance : XMLHelpers.findChildren(rootMediators, MediatorInstanceRef.XML_NODE_NAME))
				mediators.add(new MediatorInstanceRef(instance, getId()));
			for (Node spec : XMLHelpers.findChildren(rootMediators, MediatorSpecRef.XML_NODE_NAME))
				mediators.add(new MediatorSpecRef(spec, getId()));
		}

		Node rootBindings = XMLHelpers.findChild(node, XML_ROOT_BINDINGS_NAME);
		if (rootBindings != null) {
			for (Node bi : XMLHelpers.findChildren(rootBindings, Binding.XML_NODE_NAME))
				bindings.add(new Binding(bi, getId()));
		}
	}

	public List<AdapterRef> getAdapters() {
		return adapters;
	}

	public List<MediatorRef> getMediators() {
		return mediators;
	}

	public List<Binding> getBindings() {
		return bindings;
	}

	public ComponentRef getComponent(String componentId) {
		for (AdapterRef adapter : adapters)
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorRef mediator : mediators)
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	public ComponentRef[] getComponents() {
		List<ComponentRef> retval = new ArrayList<ComponentRef>();
		for (AdapterRef adapter : adapters)
			retval.add(adapter);
		for (MediatorRef mediator : mediators)
			retval.add(mediator);
		return retval.toArray(new ComponentRef[0]);
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

		ComponentRef component = getComponent(chainComponentID);
		if (component == null)
			throw new CiliaException("can't find component with id " + chainComponentID);

		NameNamespaceID referencedID = component.getReferencedTypeID();
		if (component instanceof AdapterInstanceRef) {
			IGenericAdapter adapterInstance = JarRepoService.getInstance().getAdapterForChain(referencedID);
			if (adapterInstance == null)
				throw new CiliaException("Adapter " + chainComponentID + " doesn't reference a valid adapter instance.");
			return adapterInstance;
		} else if (component instanceof MediatorSpecRef) {
			IGenericMediator mediatorSpec = SpecRepoService.getInstance().getMediatorForChain(referencedID);
			if (mediatorSpec == null)
				throw new CiliaException("Mediator " + chainComponentID
						+ " doesn't reference a valid mediator instance.");
			return mediatorSpec;
		} else if (component instanceof MediatorInstanceRef) {
			IGenericMediator mediatorInstance = JarRepoService.getInstance().getMediatorForChain(referencedID);
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

		if (src instanceof IGenericAdapter) {
			IGenericAdapter adapter = (IGenericAdapter) src;
			if (adapter.getType() == AdapterType.OUT)
				return srcElem + " is an out-adapter. It can't be a binding source.";
		}

		if (dst instanceof IGenericAdapter) {
			IGenericAdapter adapter = (IGenericAdapter) dst;
			if (adapter.getType() == AdapterType.IN)
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

		for (ComponentRef c : getComponents()) {
			try {
				getReferencedComponent(c.getId());
			} catch (CiliaException e) {
				list.add(new CiliaError(e.getMessage(), this));
			}
		}

		return CiliaFlag.generateTab(list);
	}
}
