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
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainModel;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorImplemRef;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.ComponentNatureAskable.ComponentNature;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

/**
 * A {@link DSCiliaModel} represents the content of a <strong>well
 * formed<strong> {@link DSCiliaFile}.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaModel extends ChainModel<DSCiliaChain> {

	public static final String ROOT_NODE_NAME = "cilia";

	public DSCiliaModel(File file) throws Exception {
		super(file, ROOT_NODE_NAME, DSCiliaRepoService.getInstance());

		Node root = getRootNode(getDocument());

		for (Node node : XMLHelpers.findChildren(root, DSCiliaChain.XML_NODE_NAME))
			model.add(new DSCiliaChain(node));
	}

	public void createMediator(DSCiliaChain chain, String id, IMediator type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, type.getId()) == null) {
			if (type.getNature() == ComponentNature.IMPLEM)
				createComponentInstanceInternal(chain, id, type.getId(), Chain.XML_ROOT_MEDIATORS_NAME, MediatorImplemRef.XML_NODE_NAME_FOR_DSCILIA);
			else
				throw new CiliaException("Not an implem...");
		}
	}

}
