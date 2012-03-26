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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.misc.XMLUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * Represents the content of a <strong>well formed<strong> {@link SpecFile}.
 * 
 * @author Etienne Gandrille
 */
public class SpecModel {

	/** XML Root node name */
	public static final String ROOT_NODE_NAME = "cilia-specifications";

	/** Physical file path. */
	private String filePath;

	/** Specs owned by the file. */
	private List<MediatorSpec> mediatorSpecs = new ArrayList<MediatorSpec>();

	/**
	 * Instantiates a new spec model.
	 * 
	 * @param filePath
	 *            the file path
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public SpecModel(String filePath) throws MetadataException {
		this.filePath = filePath;

		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLReflectionUtil.findChildren(root, "mediator-specification"))
			mediatorSpecs.add(new MediatorSpec(node));
	}

	/**
	 * Gets the mediator specs.
	 * 
	 * @return the mediator specs
	 */
	public List<MediatorSpec> getMediatorSpecs() {
		return mediatorSpecs;
	}

	/**
	 * Gets the root node.
	 * 
	 * @param document
	 *            the document
	 * @return the root node
	 * @throws MetadataException
	 *             the metadata exception
	 */
	private static Node getRootNode(Document document) throws MetadataException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	/**
	 * Merge.
	 * 
	 * @param newInstance
	 *            the new instance
	 * @return the changeset[]
	 */
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

	public void deleteMediator(String id, String namespace) throws MetadataException {

		// Finding target node
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLMediatorNode(document, id, namespace);

		if (target != null) {
			getRootNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			SpecRepoService.getInstance().updateModel();
		}
	}

	private Node findXMLMediatorNode(Document document, String id, String namespace) throws MetadataException {
		Node root = getRootNode(document);
		Node[] results = XMLUtil.findXMLChildNode(root, "mediator-specification", "id", id, "namespace", namespace);

		if (results.length == 0)
			return null;
		else
			return results[0];
	}
}
