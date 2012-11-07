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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.GraphDrawable;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Chain extends NameNamespace implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Mergeable, GraphDrawable {

	public static final String XML_NODE_NAME = "chain";

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public static final String XML_ROOT_MEDIATORS_NAME = "mediators";
	public static final String XML_ROOT_ADAPTERS_NAME = "adapters";
	public static final String XML_ROOT_BINDINGS_NAME = "bindings";

	protected List<AdapterRef> adapters = new ArrayList<AdapterRef>();
	protected List<MediatorRef> mediators = new ArrayList<MediatorRef>();
	protected List<Binding> bindings = new ArrayList<Binding>();

	public Chain(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");

		Node rootAdapters = XMLHelpers.findChild(node, XML_ROOT_ADAPTERS_NAME);
		if (rootAdapters != null) {
			for (Node instance : XMLHelpers.findChildren(rootAdapters, AdapterInplemRef.XML_NODE_NAME))
				try {
					adapters.add(new AdapterInplemRef(instance, getId(), getRepository()));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
		}

		Node rootMediators = XMLHelpers.findChild(node, XML_ROOT_MEDIATORS_NAME);
		if (rootMediators != null) {
			for (Node instance : XMLHelpers.findChildren(rootMediators, MediatorImplemRef.XML_NODE_NAME_FOR_DSCILIA)) {
				try {
					MediatorImplemRef mi = new MediatorImplemRef(instance, getId(), getRepository());
					mediators.add(mi);
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		Node rootBindings = XMLHelpers.findChild(node, XML_ROOT_BINDINGS_NAME);
		if (rootBindings != null) {
			for (Node bi : XMLHelpers.findChildren(rootBindings, Binding.XML_NODE_NAME))
				try {
					bindings.add(createBinding(bi, getId()));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
		}
	}

	protected abstract ChainRepoService<?, ?, ?> getRepository();

	public List<AdapterRef> getAdapters() {
		return adapters;
	}

	public List<MediatorRef> getMediators() {
		return mediators;
	}

	public abstract Binding createBinding(Node node, NameNamespaceID chainId) throws CiliaException;

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
	public Object[] getElements() {
		return getComponents();
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
	 * exception containing an error message.
	 * 
	 * @param componentID
	 * @return
	 * @throws CiliaException
	 */
	private IComponent getReferencedComponent(String componentID) throws CiliaException {

		if (Strings.isNullOrEmpty(componentID))
			throw new CiliaException("id is null or empty");

		ComponentRef component = getComponent(componentID);
		if (component == null)
			throw new CiliaException("can't find component with id " + componentID);

		NameNamespaceID referencedID = component.getReferencedTypeID();
		if (component instanceof AdapterInplemRef) {
			IAdapter adapterInstance = JarRepoService.getInstance().getAdapterForChain(referencedID);
			if (adapterInstance == null)
				throw new CiliaException("Adapter " + componentID + " doesn't reference a valid adapter instance.");
			return adapterInstance;
		} else if (component instanceof MediatorSpecRef) {
			IMediator mediatorSpec = SpecRepoService.getInstance().getMediatorForChain(referencedID);
			if (mediatorSpec == null)
				throw new CiliaException("Mediator " + componentID + " doesn't reference a valid mediator instance.");
			return mediatorSpec;
		} else if (component instanceof MediatorImplemRef) {
			IMediator mediatorInstance = JarRepoService.getInstance().getMediatorForChain(referencedID);
			if (mediatorInstance == null)
				throw new CiliaException("Mediator " + componentID + " doesn't reference a valid mediator specification.");
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
			if (adapter.getType() == AdapterType.OUT)
				return srcElem + " is an out-adapter. It can't be a binding source.";
		}

		if (dst instanceof IAdapter) {
			IAdapter adapter = (IAdapter) dst;
			if (adapter.getType() == AdapterType.IN)
				return dstElem + " is an in-adapter. It can't be a binding destination.";
		}

		return null;
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

	@Override
	public String toString() {
		return getName();
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