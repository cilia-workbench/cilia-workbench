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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaChain extends Chain {

	public DSCiliaChain(Node node) throws CiliaException {
		super(node, DSCiliaModel.XML_MEDIATOR_NODE_NAME, DSCiliaModel.XML_ADAPTER_NODE_NAME);
	}

	@Override
	public Binding createBinding(Node node, NameNamespaceID chainId) throws CiliaException {
		return new DSCiliaBinding(node, chainId);
	}

	@Override
	protected ChainRepoService<?, ?, DSCiliaChain> getRepository() {
		return DSCiliaRepoService.getInstance();
	}
}
