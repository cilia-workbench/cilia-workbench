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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.AdapterInplemRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.AdapterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainElement;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainModel;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ComponentRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorImplemRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ParameterChain;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.ComponentNatureAskable.ComponentNature;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * A {@link AbstractCompositionModel} represents the content of a <strong>well
 * formed<strong> {@link AbstractCompositionFile}.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionModel extends ChainModel<AbstractChain> {

	public static final String ROOT_NODE_NAME = "cilia-composition-specifications";

	public AbstractCompositionModel(File file) throws CiliaException {
		super(file, ROOT_NODE_NAME);

		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, ChainElement.XML_NODE_NAME))
			try {
				model.add(new AbstractChain(node));
			} catch (CiliaException e) {
				e.printStackTrace();
			}
	}

	@Override
	protected ChainRepoService<?, ?, ?> getRepository() {
		return AbstractCompositionsRepoService.getInstance();
	}

	public void createMediator(AbstractChain chain, String id, IMediator type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, type.getId()) == null) {
			if (type.getNature() == ComponentNature.SPEC)
				createComponentInstanceInternal(chain, id, type.getId(), AbstractChain.XML_ROOT_MEDIATORS_NAME,
						MediatorSpecRef.XML_NODE_NAME);
			else if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, type.getId(), AbstractChain.XML_ROOT_MEDIATORS_NAME,
						MediatorImplemRef.XML_NODE_NAME);
			else
				throw new RuntimeException("Not a spec nor an implem...");
		}
	}

	public void createAdapter(AbstractChain chain, String id, IAdapter type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, type.getId()) == null) {
			if (type.getNature() == ComponentNature.SPEC)
				throw new RuntimeException("Not yet implemented in spec repository view...");
			else if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, type.getId(), AbstractChain.XML_ROOT_ADAPTERS_NAME,
						AdapterInplemRef.XML_NODE_NAME);
			else
				throw new RuntimeException("Not a spec nor an implem...");
		}
	}

	private void createComponentInstanceInternal(AbstractChain chain, String id, NameNamespaceID type, String rootNode,
			String elementNode) throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLHelpers.getOrCreateChild(document, chainNode, rootNode);

		Element child = document.createElement(elementNode);
		child.setAttribute(ComponentRef.XML_ATTR_ID, id);
		child.setAttribute(ComponentRef.XML_ATTR_TYPE, type.getName());
		if (!Strings.isNullOrEmpty(type.getNamespace()))
			child.setAttribute(ComponentRef.XML_ATTR_NAMESPACE, type.getNamespace());
		componentNode.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, file);

		// Notifies Repository
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	public void createBinding(AbstractChain chain, String srcElem, String srcPort, String dstElem, String dstPort,
			Cardinality srcCard, Cardinality dstCard) throws CiliaException {
		if (chain.isNewBindingAllowed(srcElem, srcPort, dstElem, dstPort) == null) {

			String from;
			if (Strings.isNullOrEmpty(srcPort))
				from = srcElem;
			else
				from = srcElem + ":" + srcPort;

			String to;
			if (Strings.isNullOrEmpty(dstPort))
				to = dstElem;
			else
				to = dstElem + ":" + dstPort;

			Document document = XMLHelpers.getDocument(file);
			Node chainNode = findXMLChainNode(document, chain.getId());
			Node componentNode = XMLHelpers.getOrCreateChild(document, chainNode, AbstractChain.XML_ROOT_BINDINGS_NAME);

			Element child = document.createElement(Binding.XML_NODE_NAME);
			child.setAttribute(Binding.XML_FROM_ATTR, from);
			child.setAttribute(Binding.XML_TO_ATTR, to);
			child.setAttribute(AbstractBinding.XML_FROM_CARD_ATTR, srcCard.stringId());
			child.setAttribute(AbstractBinding.XML_TO_CARD_ATTR, dstCard.stringId());
			componentNode.appendChild(child);

			// Write it back to file system
			XMLHelpers.writeDOM(document, file);

			// Notifies Repository
			AbstractCompositionsRepoService.getInstance().updateModel();
		}
	}

	public void deleteComponent(AbstractChain chain, ComponentRef<AbstractChain> component) throws CiliaException {
		if (component instanceof AdapterRef)
			deleteAdapter(chain, (AdapterRef<AbstractChain>) component);
		else
			deleteMediator(chain, (MediatorRef<AbstractChain>) component);
	}

	private void deleteMediator(AbstractChain chain, MediatorRef<AbstractChain> mediator) throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_MEDIATORS_NAME);

		Node leafs[] = null;
		if (mediator instanceof MediatorImplemRef)
			leafs = XMLHelpers.findChildren(subNode, MediatorImplemRef.XML_NODE_NAME,
					MediatorImplemRef.XML_ATTR_ID, mediator.getId());
		if (mediator instanceof MediatorSpecRef)
			leafs = XMLHelpers.findChildren(subNode, MediatorSpecRef.XML_NODE_NAME, MediatorSpecRef.XML_ATTR_ID,
					mediator.getId());

		if (leafs == null || leafs.length == 0)
			throw new CiliaException("Can't find mediator with id " + mediator.getId() + " in XML file");

		for (Node leaf : leafs)
			subNode.removeChild(leaf);

		deleteBindingsWithReferenceToComponent(chainNode, mediator.getId());
		XMLHelpers.writeDOM(document, file);
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	private void deleteAdapter(AbstractChain chain, AdapterRef<AbstractChain> adapter) throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_ADAPTERS_NAME);

		Node leafs[] = null;
		if (adapter instanceof AdapterInplemRef)
			leafs = XMLHelpers.findChildren(subNode, AdapterInplemRef.XML_NODE_NAME, AdapterInplemRef.XML_ATTR_ID,
					adapter.getId());
		// Adapter spec...

		if (leafs == null || leafs.length == 0)
			throw new CiliaException("Can't find adapter with id " + adapter.getId() + " in XML file");

		for (Node leaf : leafs)
			subNode.removeChild(leaf);

		deleteBindingsWithReferenceToComponent(chainNode, adapter.getId());
		XMLHelpers.writeDOM(document, file);
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	private void deleteBindingsWithReferenceToComponent(Node chainNode, String componentID) throws CiliaException {
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_BINDINGS_NAME);
		if (subNode == null)
			return;

		// finds nodes
		List<Node> nodes = new ArrayList<Node>();
		Node bindings[] = XMLHelpers.findChildren(subNode, Binding.XML_NODE_NAME);
		for (Node binding : bindings) {
			String from = XMLHelpers.findAttributeValue(binding, Binding.XML_FROM_ATTR);
			String to = XMLHelpers.findAttributeValue(binding, Binding.XML_TO_ATTR);

			String fromID = XMLStringUtil.getBeforeSeparatorOrAll(from);
			String toID = XMLStringUtil.getBeforeSeparatorOrAll(to);

			if (fromID.equalsIgnoreCase(componentID) || toID.equalsIgnoreCase(componentID))
				nodes.add(binding);
		}

		// remove nodes
		for (Node binding : nodes) {
			subNode.removeChild(binding);
		}
	}

	public void deleteBinding(AbstractChain chain, Binding binding) throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_BINDINGS_NAME);
		if (subNode == null)
			return;

		Node[] nodes = XMLHelpers.findChildren(subNode, Binding.XML_NODE_NAME, Binding.XML_FROM_ATTR,
				binding.getSource(), Binding.XML_TO_ATTR, binding.getDestination());
		if (nodes.length == 0)
			throw new CiliaException("Can't find binding " + binding);
		subNode.removeChild(nodes[0]);

		XMLHelpers.writeDOM(document, file);
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	public void updateProperties(AbstractChain chain, MediatorSpecRef<AbstractChain> mediator,
			Map<String, String> properties) throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_MEDIATORS_NAME);
		Node media = XMLHelpers.findChildren(subNode, MediatorSpecRef.XML_NODE_NAME, MediatorSpecRef.XML_ATTR_ID,
				mediator.getId())[0];

		// first, delete
		for (Node n : XMLHelpers.findChildren(media, MediatorSpecRef.XML_SELECTION_CONSTRAINT))
			media.removeChild(n);

		// then (re)create
		Node select = XMLHelpers.getOrCreateChild(document, media, MediatorSpecRef.XML_SELECTION_CONSTRAINT);
		for (String key : properties.keySet()) {
			XMLHelpers.createChild(document, select, PropertyConstraint.XML_PROPERTY_CONSTRAINT,
					PropertyConstraint.XML_ATTR_NAME, key, PropertyConstraint.XML_ATTR_VALUE, properties.get(key));
		}

		XMLHelpers.writeDOM(document, file);
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	public void updateParameters(AbstractChain chain, MediatorRef<AbstractChain> mediator,
			Map<String, String> schedulerParam, Map<String, String> processorParam, Map<String, String> dispatcherParam)
			throws CiliaException {
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_MEDIATORS_NAME);

		Node media = null;
		if (mediator instanceof MediatorSpecRef)
			media = XMLHelpers.findChildren(subNode, MediatorSpecRef.XML_NODE_NAME, MediatorSpecRef.XML_ATTR_ID,
					mediator.getId())[0];
		else
			media = XMLHelpers.findChildren(subNode, MediatorImplemRef.XML_NODE_NAME,
					MediatorImplemRef.XML_ATTR_ID, mediator.getId())[0];

		updateParameterInternal(document, media, MediatorRef.XML_SCHEDULER_NODE, schedulerParam);
		updateParameterInternal(document, media, MediatorRef.XML_PROCESSOR_NODE, processorParam);
		updateParameterInternal(document, media, MediatorRef.XML_DISPATCHER_NODE, dispatcherParam);

		XMLHelpers.writeDOM(document, file);
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	private static void updateParameterInternal(Document document, Node mediatorRoot, String xmlPartName,
			Map<String, String> parameters) {

		// first, delete
		Node sub = XMLHelpers.findChild(mediatorRoot, xmlPartName);
		if (sub != null)
			for (Node n : XMLHelpers.findChildren(sub, ParameterChain.XML_ROOT_NAME))
				sub.removeChild(n);

		// then (re)create
		if (parameters.size() != 0) {
			if (sub == null)
				sub = XMLHelpers.createChild(document, mediatorRoot, xmlPartName);
			for (String key : parameters.keySet())
				XMLHelpers.createChild(document, sub, ParameterChain.XML_ROOT_NAME, ParameterChain.XML_ATTR_NAME, key,
						ParameterChain.XML_ATTR_VALUE, parameters.get(key));
		}
	}
}
