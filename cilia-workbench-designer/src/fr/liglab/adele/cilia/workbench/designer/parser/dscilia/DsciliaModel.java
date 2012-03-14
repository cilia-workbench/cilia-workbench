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

import fr.liglab.adele.cilia.workbench.common.misc.XMLUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;


/**
 * A {@link DsciliaModel} represents the content of a <strong>well formed<strong> {@link DsciliaFile}.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaModel {
	
	private String filePath;
	
	private List<Chain> chains = new ArrayList<Chain>();

	public DsciliaModel(String filePath) throws Exception {
		this.filePath = filePath;

		Document document = XMLHelpers.getDocument(filePath);
		Node root = getCiliaNode(document);

		for (Node node : XMLReflectionUtil.findChildren(root, "chain"))
			chains.add(new Chain(node));
	}

	private static Node getCiliaNode(Document document) throws MetadataException {
		return XMLHelpers.getRootNode(document, "cilia");
	}

	public void createChain(String chainName) throws MetadataException {

		// Document creation
		Document document = XMLHelpers.getDocument(filePath);
		Node root = getCiliaNode(document);
		Element child = document.createElement("chain");
		child.setAttribute("id", chainName);
		root.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	public void deleteChain(String id) throws MetadataException {
		// Finding target node
		Document document = XMLHelpers.getDocument(filePath);
		Node target = findXMLChainNode(document, id);

		if (target != null) {
			getCiliaNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			DsciliaRepoService.getInstance().updateModel();
		}
	}

	private Node findXMLChainNode(Document document, String chainId) throws MetadataException {
		Node root = getCiliaNode(document);
		Node[] results = XMLUtil.findXMLChildNode(root, "chain", "id", chainId);

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	public void createMediatorInstance(Chain chain, String id, String type) throws MetadataException {
		if (chain.isNewMediatorInstanceAllowed(id, type) == null)
			createComponentInstanceInternal(chain, id, type, "mediator");
	}

	public void createAdapterInstance(Chain chain, String id, String type) throws MetadataException {
		if (chain.isNewAdapterInstanceAllowed(id, type) == null)
			createComponentInstanceInternal(chain, id, type, "adapter");
	}

	private void createComponentInstanceInternal(Chain chain, String id, String type, String componentName)
			throws MetadataException {
		Document document = XMLHelpers.getDocument(filePath);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLUtil.getOrCreateSubNode(document, chainNode, componentName + "s");

		Element child = document.createElement(componentName + "-instance");
		child.setAttribute("id", id);
		child.setAttribute("type", type);
		componentNode.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	public void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort) throws MetadataException {
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
			
			Document document = XMLHelpers.getDocument(filePath);
			Node chainNode = findXMLChainNode(document, chain.getId());
			Node componentNode = XMLUtil.getOrCreateSubNode(document, chainNode, "bindings");

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

	@Override
	public String toString() {
		int index = filePath.lastIndexOf(File.separator, filePath.length());
		if (index == -1)
			return filePath;
		else
			return filePath.substring(index + 1);
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
