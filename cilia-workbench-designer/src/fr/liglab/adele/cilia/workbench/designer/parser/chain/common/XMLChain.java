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
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.AdapterImplemRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.MediatorImplemRef;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.GraphDrawable;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class XMLChain extends Chain implements GraphDrawable {

	public static final String XML_NODE_NAME = "chain";

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public static final String XML_ROOT_MEDIATORS_NAME = "mediators";
	public static final String XML_ROOT_ADAPTERS_NAME = "adapters";
	public static final String XML_ROOT_BINDINGS_NAME = "bindings";

	protected NameNamespaceID id = new NameNamespaceID();

	public XMLChain(Node node, String mediatorXMLNodeName, String adapterXMLNodeName) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, id, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, id, "namespace");

		Node rootAdapters = XMLHelpers.findChild(node, XML_ROOT_ADAPTERS_NAME);
		if (rootAdapters != null) {
			for (Node instance : XMLHelpers.findChildren(rootAdapters, adapterXMLNodeName))
				try {
					adapters.add(new AdapterImplemRef(instance, getId(), getRepository()));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
		}

		Node rootMediators = XMLHelpers.findChild(node, XML_ROOT_MEDIATORS_NAME);
		if (rootMediators != null) {
			for (Node instance : XMLHelpers.findChildren(rootMediators, mediatorXMLNodeName)) {
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
			for (Node bi : XMLHelpers.findChildren(rootBindings, XMLBinding.XML_NODE_NAME))
				try {
					bindings.add(createBinding(bi, getId()));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public NameNamespaceID getId() {
		return id;
	}

	@Override
	public String getName() {
		return id.getName();
	}

	protected abstract ChainRepoService<?, ?, ?> getRepository();

	public abstract XMLBinding createBinding(Node node, NameNamespaceID chainId) throws CiliaException;

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

	public String isNewBindingAllowed(String srcElem, String srcPort, String dstElem, String dstPort) {

		ComponentDefinition src;
		ComponentDefinition dst;
		if (srcElem.equalsIgnoreCase(dstElem))
			return "Source and destination can't be the same";

		try {
			src = getReferencedComponent(srcElem);
			dst = getReferencedComponent(dstElem);
		} catch (CiliaException e) {
			return e.getMessage();
		}

		if (src instanceof Adapter) {
			Adapter adapter = (Adapter) src;
			if (adapter.getType() == AdapterType.OUT)
				return srcElem + " is an out-adapter. It can't be a binding source.";
		}

		if (dst instanceof Adapter) {
			Adapter adapter = (Adapter) dst;
			if (adapter.getType() == AdapterType.IN)
				return dstElem + " is an in-adapter. It can't be a binding destination.";
		}

		// Port checking
		if (!src.hasOutPort(srcPort))
			return "unknown " + srcElem + " out-port with name " + srcPort;
		if (!dst.hasInPort(dstPort))
			return "unknown " + dstElem + " in-port with name " + dstPort;

		return null;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id.getName(), "name");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, id.getNamespace(), "namespace");

		// In the future, move this to chain Class ?
		List<CiliaFlag> list = new ArrayList<CiliaFlag>();
		for (ComponentRef c : getComponents()) {
			try {
				getReferencedComponent(c.getId());
			} catch (CiliaException e) {
				list.add(new CiliaError(e.getMessage(), this));
			}
		}

		return CiliaFlag.generateTab(tab, list.toArray(new CiliaFlag[0]), e1, e2);
	}
}
