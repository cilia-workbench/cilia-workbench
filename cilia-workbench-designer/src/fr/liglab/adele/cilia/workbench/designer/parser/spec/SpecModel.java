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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaModel;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.PullElementUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

public class SpecModel {

	private String filePath;
	
	private List<MediatorSpec> mediatorSpecs = new ArrayList<MediatorSpec>();
	
	public SpecModel(String filePath) throws MetadataException {
		this.filePath = filePath;
		
		Document document = XMLHelpers.getDocument(filePath);
		Node root = getSpecNode(document);

		for (Node node : XMLReflectionUtil.findChildren(root, "mediator-specification"))
			mediatorSpecs.add(new MediatorSpec(node));
	}
	
	public List<MediatorSpec> getMediatorSpecs() {
		return mediatorSpecs;
	}
	
	private static Node getSpecNode(Document document) throws MetadataException {
		return XMLHelpers.getRootNode(document, "cilia-specifications");
	}

	public Changeset[] merge(SpecModel newInstance) {
		
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<MediatorSpec> itr = mediatorSpecs.iterator(); itr.hasNext();) {
			MediatorSpec old = itr.next();
			String id = old.getId();
			String namespace = old.getNamespace();
			
			MediatorSpec updated = PullElementUtil.pullMediatorSpec(newInstance, id, namespace);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (MediatorSpec c : newInstance.getMediatorSpecs()) {
			mediatorSpecs.add(c);
			retval.add(new Changeset(Operation.ADD, c));
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}
}
