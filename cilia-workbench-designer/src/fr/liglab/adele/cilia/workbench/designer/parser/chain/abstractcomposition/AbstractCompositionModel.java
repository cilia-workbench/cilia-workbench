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

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition.ComponentNature;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChainModel;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLComponentRefHelper;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLParameterRef;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;

/**
 * A {@link AbstractCompositionModel} represents the content of a <strong>well
 * formed<strong> {@link AbstractCompositionFile}.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionModel extends XMLChainModel<AbstractChain> {

	public static final String ROOT_NODE_NAME = "cilia-composition-specifications";

	public static final String XML_ADAPTER_NODE_NAME = "adapter-implem";
	public static final String XML_MEDIATOR_NODE_NAME = "mediator-implem";

	public AbstractCompositionModel(PhysicalResource file) throws CiliaException {
		super(file, ROOT_NODE_NAME, AbstractCompositionsRepoService.getInstance());

		Node root = getRootNode(getDocument());

		for (Node node : XMLHelpers.findChildren(root, XMLChain.XML_NODE_NAME))
			model.add(new AbstractChain(node));
	}

	public void createMediator(AbstractChain chain, String id, Mediator type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, (NameNamespaceID) type.getId()) == null) {
			if (type.getNature() == ComponentNature.SPEC)
				createComponentInstanceInternal(chain, id, (NameNamespaceID) type.getId(), XMLChain.XML_ROOT_MEDIATORS_NAME, MediatorSpecRef.XML_NODE_NAME);
			else if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, (NameNamespaceID) type.getId(), XMLChain.XML_ROOT_MEDIATORS_NAME, XML_MEDIATOR_NODE_NAME);
			else
				throw new CiliaException("Not a spec nor an implem...");
		}
	}

	public void createAdapter(AbstractChain chain, String id, Adapter type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, (NameNamespaceID) type.getId()) == null) {
			if (type.getNature() == ComponentNature.SPEC)
				throw new RuntimeException("Not yet implemented in spec repository view...");
			else if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, (NameNamespaceID) type.getId(), XMLChain.XML_ROOT_ADAPTERS_NAME, XML_ADAPTER_NODE_NAME);
			else
				throw new RuntimeException("Not a spec nor an implem...");
		}
	}

	public void deleteComponent(XMLChain chain, ComponentRef component) throws CiliaException {
		if (component instanceof AdapterRef)
			deleteAdapter(chain, (AdapterRef) component, XML_ADAPTER_NODE_NAME);
		else
			deleteMediator(chain, (MediatorRef) component, XML_MEDIATOR_NODE_NAME);
	}

	public void updateProperties(AbstractChain chain, MediatorSpecRef mediator, Map<String, String> properties) throws CiliaException {
		Document document = getDocument();
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_MEDIATORS_NAME);
		Node media = XMLHelpers.findChildren(subNode, MediatorSpecRef.XML_NODE_NAME, XMLComponentRefHelper.XML_ATTR_ID, mediator.getId())[0];

		// first, delete
		for (Node n : XMLHelpers.findChildren(media, MediatorSpecRef.XML_SELECTION_CONSTRAINT))
			media.removeChild(n);

		// then (re)create
		Node select = XMLHelpers.getOrCreateChild(document, media, MediatorSpecRef.XML_SELECTION_CONSTRAINT);
		for (String key : properties.keySet()) {
			XMLHelpers.createChild(document, select, PropertyConstraint.XML_PROPERTY_CONSTRAINT, PropertyConstraint.XML_ATTR_NAME, key,
					PropertyConstraint.XML_ATTR_VALUE, properties.get(key));
		}

		writeToFile(document);
		notifyRepository();
	}

	public void updateParameters(AbstractChain chain, MediatorRef mediator, Map<String, String> schedulerParam, Map<String, String> processorParam,
			Map<String, String> dispatcherParam) throws CiliaException {
		Document document = getDocument();
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node subNode = XMLHelpers.findChild(chainNode, AbstractChain.XML_ROOT_MEDIATORS_NAME);

		Node media = null;
		if (mediator instanceof MediatorSpecRef)
			media = XMLHelpers.findChildren(subNode, MediatorSpecRef.XML_NODE_NAME, XMLComponentRefHelper.XML_ATTR_ID, mediator.getId())[0];
		else
			media = XMLHelpers.findChildren(subNode, XML_MEDIATOR_NODE_NAME, XMLComponentRefHelper.XML_ATTR_ID, mediator.getId())[0];

		updateParameterInternal(document, media, MediatorSpecRef.XML_SCHEDULER_NODE, schedulerParam);
		updateParameterInternal(document, media, MediatorSpecRef.XML_PROCESSOR_NODE, processorParam);
		updateParameterInternal(document, media, MediatorSpecRef.XML_DISPATCHER_NODE, dispatcherParam);

		writeToFile(document);
		notifyRepository();
	}

	private static void updateParameterInternal(Document document, Node mediatorRoot, String xmlPartName, Map<String, String> parameters) {

		// first, delete
		Node sub = XMLHelpers.findChild(mediatorRoot, xmlPartName);
		if (sub != null)
			for (Node n : XMLHelpers.findChildren(sub, XMLParameterRef.XML_ROOT_NAME))
				sub.removeChild(n);

		// then (re)create
		if (parameters.size() != 0) {
			if (sub == null)
				sub = XMLHelpers.createChild(document, mediatorRoot, xmlPartName);
			for (String key : parameters.keySet())
				XMLHelpers.createChild(document, sub, XMLParameterRef.XML_ROOT_NAME, XMLParameterRef.XML_ATTR_NAME, key, XMLParameterRef.XML_ATTR_VALUE,
						parameters.get(key));
		}
	}
}
