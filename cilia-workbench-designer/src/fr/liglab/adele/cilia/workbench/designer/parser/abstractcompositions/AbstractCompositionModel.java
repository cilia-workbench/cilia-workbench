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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.SpecImplemAskable.Nature;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * A {@link AbstractCompositionModel} represents the content of a <strong>well
 * formed<strong> {@link AbstractCompositionFile}.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionModel implements DisplayedInPropertiesView, Mergeable {

	/** XML Root node name */
	public static final String ROOT_NODE_NAME = "cilia-composition-specifications";

	/** Physical file path */
	private String filePath;

	/** Chains owned by this file */
	private List<Chain> chains = new ArrayList<Chain>();

	public AbstractCompositionModel(String filePath) throws Exception {
		this.filePath = filePath;

		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, Chain.XML_NODE_NAME))
			chains.add(new Chain(node));
	}

	public List<Chain> getChains() {
		return chains;
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		AbstractCompositionModel newInstance = (AbstractCompositionModel) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getChains(), chains));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public void createChain(NameNamespaceID id) throws CiliaException {

		// Document creation
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);
		Element child = document.createElement(Chain.XML_NODE_NAME);
		child.setAttribute(Chain.XML_ATTR_ID, id.getName());
		child.setAttribute(Chain.XML_ATTR_NAMESPACE, id.getNamespace());
		root.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		AbstractCompositionsRepoService.getInstance().updateModel();
	}

	public void deleteChain(NameNamespaceID id) throws CiliaException {

		// Finding target node
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLChainNode(document, id);

		if (target != null) {
			getRootNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			AbstractCompositionsRepoService.getInstance().updateModel();
		}
	}

	private Node findXMLChainNode(Document document, NameNamespaceID id) throws CiliaException {
		Node root = getRootNode(document);
		Node[] results;

		if (Strings.isNullOrEmpty(id.getNamespace()))
			results = XMLHelpers.findChildren(root, Chain.XML_NODE_NAME, Chain.XML_ATTR_ID, id.getName());
		else
			results = XMLHelpers.findChildren(root, Chain.XML_NODE_NAME, Chain.XML_ATTR_ID, id.getName(),
					Chain.XML_ATTR_NAMESPACE, id.getNamespace());

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	public void createMediator(Chain chain, String id, IMediator type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, type.getId()) == null) {
			if (type.getNature() == Nature.SPEC)
				createComponentInstanceInternal(chain, id, type.getId(), "mediators", MediatorSpec.XML_NODE_NAME);
			else if (type.getNature() == Nature.IMPLEM)
				createComponentInstanceInternal(chain, id, type.getId(), "mediators", MediatorInstance.XML_NODE_NAME);
			else
				throw new RuntimeException("Not a spec nor an implem...");
		}
	}

	public void createAdapter(Chain chain, String id, IAdapter type) throws CiliaException {
		if (chain.isNewComponentAllowed(id, type.getId()) == null) {
			if (type.getNature() == Nature.SPEC)
				throw new RuntimeException("Not yet implemented in spec repository view...");
			else if (type.getNature() == Nature.IMPLEM)
				createComponentInstanceInternal(chain, id, type.getId(), "adapters", AdapterInstance.XML_NODE_NAME);
			else
				throw new RuntimeException("Not a spec nor an implem...");
		}
	}

	private void createComponentInstanceInternal(Chain chain, String id, NameNamespaceID type, String rootNode,
			String elementNode) throws CiliaException {
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLHelpers.getOrCreateNode(document, chainNode, rootNode);

		Element child = document.createElement(elementNode);
		child.setAttribute("id", id);
		child.setAttribute("type", type.getName());
		if (!Strings.isNullOrEmpty(type.getNamespace()))
			child.setAttribute("namespace", type.getNamespace());
		componentNode.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		AbstractCompositionsRepoService.getInstance().updateModel();
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
			AbstractCompositionsRepoService.getInstance().updateModel();
		}
	}
}
