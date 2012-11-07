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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractModel;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractBinding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainModel<ChainType extends Chain> extends AbstractModel implements Mergeable {

	protected List<ChainType> model = new ArrayList<ChainType>();
	protected final ChainRepoService<?, ?, ChainType> repository;

	public ChainModel(File file, String rootNodeName, ChainRepoService<?, ?, ChainType> repository) {
		super(file, rootNodeName);
		this.repository = repository;
	}

	public List<ChainType> getChains() {
		return model;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		@SuppressWarnings("unchecked")
		ChainModel<ChainType> newInstance = (ChainModel<ChainType>) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getChains(), model));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public void createChain(NameNamespaceID id) throws CiliaException {

		// Document creation
		Document document = getDocument();
		Node root = getRootNode(document);
		Element child = document.createElement(Chain.XML_NODE_NAME);
		child.setAttribute(Chain.XML_ATTR_ID, id.getName());
		child.setAttribute(Chain.XML_ATTR_NAMESPACE, id.getNamespace());
		root.appendChild(child);

		writeToFile(document);
		notifyRepository();
	}

	public void deleteChain(NameNamespaceID id) throws CiliaException {

		// Finding target node
		Document document = getDocument();
		Node target = findXMLChainNode(document, id);

		if (target != null) {
			getRootNode(document).removeChild(target);
			writeToFile(document);
			notifyRepository();
		}
	}

	protected void createComponentInstanceInternal(Chain chain, String id, NameNamespaceID type, String rootNode, String elementNode) throws CiliaException {
		Document document = getDocument();
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLHelpers.getOrCreateChild(document, chainNode, rootNode);

		Element child = document.createElement(elementNode);
		child.setAttribute(ComponentRef.XML_ATTR_ID, id);
		child.setAttribute(ComponentRef.XML_ATTR_TYPE, type.getName());
		if (!Strings.isNullOrEmpty(type.getNamespace()))
			child.setAttribute(ComponentRef.XML_ATTR_NAMESPACE, type.getNamespace());
		componentNode.appendChild(child);

		writeToFile(document);
		notifyRepository();
	}

	/**
	 * srcCard and dstCard can be null. If they are, cardinalities are not
	 * written in the XML file
	 */
	public void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort, Cardinality srcCard, Cardinality dstCard)
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

			Document document = getDocument();
			Node chainNode = findXMLChainNode(document, chain.getId());
			Node componentNode = XMLHelpers.getOrCreateChild(document, chainNode, AbstractChain.XML_ROOT_BINDINGS_NAME);

			Element child = document.createElement(Binding.XML_NODE_NAME);
			child.setAttribute(Binding.XML_FROM_ATTR, from);
			child.setAttribute(Binding.XML_TO_ATTR, to);
			if (srcCard != null)
				child.setAttribute(AbstractBinding.XML_FROM_CARD_ATTR, srcCard.stringId());
			if (dstCard != null)
				child.setAttribute(AbstractBinding.XML_TO_CARD_ATTR, dstCard.stringId());
			componentNode.appendChild(child);

			writeToFile(document);
			notifyRepository();
		}
	}

	protected Node findXMLChainNode(Document document, NameNamespaceID id) throws CiliaException {
		Node root = getRootNode(document);
		Node[] results;

		if (Strings.isNullOrEmpty(id.getNamespace()))
			results = XMLHelpers.findChildren(root, AbstractChain.XML_NODE_NAME, AbstractChain.XML_ATTR_ID, id.getName());
		else
			results = XMLHelpers.findChildren(root, AbstractChain.XML_NODE_NAME, AbstractChain.XML_ATTR_ID, id.getName(), AbstractChain.XML_ATTR_NAMESPACE,
					id.getNamespace());

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	protected void notifyRepository() {
		repository.updateModel();
	}
}
