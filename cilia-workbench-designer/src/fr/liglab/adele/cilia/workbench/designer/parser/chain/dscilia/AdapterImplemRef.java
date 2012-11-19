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
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.IChain;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLComponentRefHelper;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class AdapterImplemRef extends AdapterRef {

	/** the chainID, which hosts the current {@link ComponentRef} */
	private NameNamespaceID chainId;

	private final ChainRepoService<?, ?, ?> repo;

	public AdapterImplemRef(Node node, NameNamespaceID chainId, ChainRepoService<?, ?, ?> repo) throws CiliaException {
		super(XMLComponentRefHelper.getId(node), new NameNamespaceID(XMLComponentRefHelper.getType(node), XMLComponentRefHelper.getNamespace(node)));
		this.chainId = chainId;
		this.repo = repo;
	}

	@Override
	public Adapter getReferencedComponent() {
		NameNamespaceID id = getReferencedTypeID();
		return JarRepoService.getInstance().getAdapterForChain(id);
	}

	@Override
	public IChain getChain() {
		return repo.findChain(chainId);
	}
}