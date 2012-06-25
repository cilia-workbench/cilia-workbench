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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainElement;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChain extends ChainElement<AbstractChain> {

	public AbstractChain(Node node) throws CiliaException {
		super(node);

		// Adapters specifications references must be added here as soon as they
		// will be implemented

		// Mediators spec
		Node rootMediators = XMLHelpers.findChild(node, XML_ROOT_MEDIATORS_NAME);
		if (rootMediators != null) {
			for (Node spec : XMLHelpers.findChildren(rootMediators, MediatorSpecRef.XML_NODE_NAME))
				try {
					mediators.add(new MediatorSpecRef<AbstractChain>(spec, getId(), getRepository()));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	protected ChainRepoService<?, ?, AbstractChain> getRepository() {
		return AbstractCompositionsRepoService.getInstance();
	}

	@Override
	public Binding createBinding(Node node, NameNamespaceID chainId) throws CiliaException {
		return new AbstractBinding(node, chainId);
	}
}