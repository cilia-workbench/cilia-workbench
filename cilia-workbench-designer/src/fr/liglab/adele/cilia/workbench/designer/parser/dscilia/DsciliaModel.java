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
package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

/**
 * A {@link DsciliaModel} represents the content of a <strong>well
 * formed<strong> {@link DsciliaFile}.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaModel {

	/** XML Root node name */
	public static final String ROOT_NODE_NAME = "cilia";

	/** Physical file path */
	private String filePath;

	/** Chains owned by this file */
	private List<Chain> chains = new ArrayList<Chain>();

	public DsciliaModel(String filePath) throws Exception {
		this.filePath = filePath;

		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, "chain"))
			chains.add(new Chain(node));
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	public void createChain(String chainName) throws CiliaException {

		// Document creation
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);
		Element child = document.createElement("chain");
		child.setAttribute("id", chainName);
		root.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	public void deleteChain(String id) throws CiliaException {

		// Finding target node
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLChainNode(document, id);

		if (target != null) {
			getRootNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			DsciliaRepoService.getInstance().updateModel();
		}
	}

	private Node findXMLChainNode(Document document, String chainId) throws CiliaException {
		Node root = getRootNode(document);
		Node[] results = XMLHelpers.findChildren(root, "chain", "id", chainId);

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	public void createMediatorInstance(Chain chain, String id, NameNamespaceID type) throws CiliaException {
		if (chain.isNewMediatorInstanceAllowed(id, type) == null)
			createComponentInstanceInternal(chain, id, type, "mediator");
	}

	public void createAdapterInstance(Chain chain, String id, NameNamespaceID type) throws CiliaException {
		if (chain.isNewAdapterInstanceAllowed(id, type) == null)
			createComponentInstanceInternal(chain, id, type, "adapter");
	}

	private void createComponentInstanceInternal(Chain chain, String id, NameNamespaceID type, String componentName)
			throws CiliaException {
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLHelpers.getOrCreateNode(document, chainNode, componentName + "s");

		Element child = document.createElement(componentName + "-instance");
		child.setAttribute("id", id);
		child.setAttribute("type", type.getName());
		child.setAttribute("namespace", type.getNamespace());
		componentNode.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	public void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort)
			throws CiliaException {
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

			File file = new File(filePath);
			Document document = XMLHelpers.getDocument(file);
			Node chainNode = findXMLChainNode(document, chain.getId());
			Node componentNode = XMLHelpers.getOrCreateNode(document, chainNode, "bindings");

			Element child = document.createElement("binding");
			child.setAttribute("from", from);
			child.setAttribute("to", to);
			componentNode.appendChild(child);

			// Write it back to file system
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			DsciliaRepoService.getInstance().updateModel();
		}
	}

	public List<Chain> getChains() {
		return chains;
	}

	public String getFilePath() {
		return filePath;
	}

	public Changeset[] merge(DsciliaModel newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<Chain> itr = chains.iterator(); itr.hasNext();) {
			Chain old = itr.next();
			String id = old.getId();
			Chain updated = PullElementUtil.pullChain(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (Chain c : newInstance.getChains()) {
			chains.add(c);
			retval.add(new Changeset(Operation.ADD, c));
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}
}
