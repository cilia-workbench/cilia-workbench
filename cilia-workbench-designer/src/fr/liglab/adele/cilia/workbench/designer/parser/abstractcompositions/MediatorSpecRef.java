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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpecRef extends MediatorRef {

	public static final String XML_NODE_NAME = "mediator-specification";

	public MediatorSpecRef(Node node, NameNamespaceID chainId) throws CiliaException {
		super(node, chainId);
	}

	@Override
	public IGenericMediator getReferencedObject() {
		NameNamespaceID id = getReferencedTypeID();
		return SpecRepoService.getInstance().getMediatorForChain(id);
	}

}
