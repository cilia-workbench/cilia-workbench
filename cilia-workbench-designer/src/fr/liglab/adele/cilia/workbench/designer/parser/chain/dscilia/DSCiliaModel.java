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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia;

import java.io.File;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition.ComponentNature;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChainModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

/**
 * A {@link DSCiliaModel} represents the content of a <strong>well
 * formed<strong> {@link DSCiliaFile}.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaModel extends XMLChainModel<DSCiliaChain> {

	public static final String ROOT_NODE_NAME = "cilia";

	public static final String XML_ADAPTER_NODE_NAME = "adapter-instance";
	public static final String XML_MEDIATOR_NODE_NAME = "mediator-instance";

	public DSCiliaModel(File file) throws Exception {
		super(file, ROOT_NODE_NAME, DSCiliaRepoService.getInstance());

		Node root = getRootNode(getDocument());

		for (Node node : XMLHelpers.findChildren(root, DSCiliaChain.XML_NODE_NAME))
			model.add(new DSCiliaChain(node));
	}

	public void createMediator(DSCiliaChain chain, String id, Mediator type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, (NameNamespaceID) type.getId()) == null) {
			if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, (NameNamespaceID) type.getId(), XMLChain.XML_ROOT_MEDIATORS_NAME, XML_MEDIATOR_NODE_NAME);
			else
				throw new CiliaException("Not an implem...");
		}
	}

	public void createAdapter(DSCiliaChain chain, String id, Adapter type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, (NameNamespaceID) type.getId()) == null) {
			if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, (NameNamespaceID) type.getId(), XMLChain.XML_ROOT_ADAPTERS_NAME, XML_ADAPTER_NODE_NAME);
			else
				throw new RuntimeException("Not an implem...");
		}
	}

	public void deleteComponent(XMLChain chain, ComponentRef component) throws CiliaException {
		if (component instanceof AdapterRef)
			deleteAdapter(chain, (AdapterRef) component, XML_ADAPTER_NODE_NAME);
		else
			deleteMediator(chain, (MediatorRef) component, XML_MEDIATOR_NODE_NAME);
	}
}
