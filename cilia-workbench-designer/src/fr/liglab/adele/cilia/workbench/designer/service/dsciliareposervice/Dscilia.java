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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.misc.XMLUtil;
import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * Represents a VALID DSCilia file.
 * 
 * @see {@link RepoElement}.
 * 
 * @author Etienne Gandrille
 */
public class Dscilia {

	/** The file path to the DSCilia file. */
	private final String filePath;

	/** The chains contained by this DSCilia file. */
	private List<Chain> chains = new ArrayList<Chain>();

	/**
	 * Instantiates a new dscilia, using reflection on the DOM model.
	 * 
	 * @param filePath
	 *            the file path
	 * @throws Exception
	 *             error while parsing the DSCilia file.
	 */
	public Dscilia(String filePath) throws MetadataException {
		this.filePath = filePath;

		Document document = getDocument();
		Node root = getCiliaNode(document);

		NodeList elements = root.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Node child = elements.item(i);

			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = child.getNodeName().toLowerCase();

				if (nodeName.equals("chain"))
					chains.add(new Chain(child));
			}
		}
	}

	/**
	 * Gets the cilia node in the XML file.
	 * 
	 * @param document
	 *            the document
	 * @return the cilia node
	 * @throws MetadataException
	 *             error while parsing the DSCilia file.
	 */
	private Node getCiliaNode(Document document) throws MetadataException {
		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase("cilia"))
			return nodes.item(0);
		else
			throw new MetadataException("Can't find cilia root in " + filePath);
	}

	/**
	 * Gets the XML document.
	 * 
	 * @return the XML document
	 * @throws MetadataException
	 *             error while parsing the DSCilia file.
	 */
	private Document getDocument() throws MetadataException {
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur;
		try {
			constructeur = fabrique.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new MetadataException("Can't get document builder " + filePath, e);
		}

		File file = new File(filePath);
		Document document;
		try {
			document = constructeur.parse(file);
		} catch (SAXException e) {
			throw new MetadataException("Can't parse document " + filePath, e);
		} catch (IOException e) {
			throw new MetadataException("Can't parse document " + filePath, e);
		}

		return document;
	}

	/**
	 * Creates a chain in the XML document. Does NOT check if chain already
	 * exists.
	 * 
	 * @param chainName
	 *            the chain name
	 * @throws MetadataException
	 *             error while parsing the DSCilia file.
	 */
	protected void createChain(String chainName) throws MetadataException {

		// Document creation
		Document document = getDocument();
		Node root = getCiliaNode(document);
		Element child = document.createElement("chain");
		child.setAttribute("id", chainName);
		root.appendChild(child);

		// Write it back to file system
		writeDOM(document);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	/**
	 * Write the XML DOM document back to the file system.
	 * 
	 * @param document
	 *            the XML DOM document
	 * @throws MetadataException
	 *             XML error
	 */
	private void writeDOM(Document document) throws MetadataException {
		Source source = new DOMSource(document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer xformer = transformerFactory.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new File(filePath));
			xformer.transform(source, result);
		} catch (TransformerException e) {
			throw new MetadataException("XML transformer error", e);
		}
	}

	/**
	 * Deletes a chain in the XML document.
	 * 
	 * @param id
	 *            the chain id
	 * @throws MetadataException
	 *             XML error or chain not found.
	 */
	protected void deleteChain(String id) throws MetadataException {
		// Finding target node
		Document document = getDocument();
		Node target = findXMLChainNode(document, id);

		if (target != null) {
			getCiliaNode(document).removeChild(target);
			writeDOM(document);

			// Notifies Repository
			DsciliaRepoService.getInstance().updateModel();
		} else
			throw new MetadataException("Chain with id " + id + " not found.");
	}

	/**
	 * Finds a chain node in the XML document.
	 * 
	 * @param document
	 *            the XML document
	 * @param chainId
	 *            the chain id
	 * @return the node, or null if not found.
	 * @throws MetadataException
	 *             DOM XML error
	 */
	private Node findXMLChainNode(Document document, String chainId) throws MetadataException {
		Node root = getCiliaNode(document);
		Node[] results = XMLUtil.findXMLChildNode(root, "chain", "id", chainId);

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	/**
	 * Creates a mediator instance in the XML document. Does NOT check if
	 * mediator already exists.
	 * 
	 * @param chain
	 *            the chain id
	 * @param id
	 *            the mediator id
	 * @param type
	 *            the mediator type
	 * @throws MetadataException
	 *             XML error
	 */
	protected void createMediatorInstance(Chain chain, String id, String type) throws MetadataException {
		createComponentInstanceInternal(chain, id, type, "mediator");
	}

	/**
	 * Creates an adapter instance in the XML document. Does NOT check if
	 * adapter already exists.
	 * 
	 * @param chain
	 *            the chain id
	 * @param id
	 *            the adapter id
	 * @param type
	 *            the adapter type
	 * @throws MetadataException
	 *             XML error
	 */
	protected void createAdapterInstance(Chain chain, String id, String type) throws MetadataException {
		createComponentInstanceInternal(chain, id, type, "adapter");
	}

	/**
	 * Creates a component instance (mediator or adapter). Does NOT check if
	 * component already exists.
	 * 
	 * @param chain
	 *            the chain id
	 * @param id
	 *            the component id
	 * @param type
	 *            the component type
	 * @param componentName
	 *            the component name
	 * @throws MetadataException
	 *             XML error
	 */
	private void createComponentInstanceInternal(Chain chain, String id, String type, String componentName)
			throws MetadataException {
		Document document = getDocument();
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLUtil.getOrCreateSubNode(document, chainNode, componentName + "s");

		Element child = document.createElement(componentName + "-instance");
		child.setAttribute("id", id);
		child.setAttribute("type", type);
		componentNode.appendChild(child);

		// Write it back to file system
		writeDOM(document);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	/**
	 * Creates a binding. Does NOT check if binding already exists.
	 * 
	 * @param chain
	 *            the chain id
	 * @param srcElem
	 *            the source element id (mediator or adapter)
	 * @param srcPort
	 *            the source element port (mediator or adapter); CAN be NULL
	 * @param dstElem
	 *            the destination element id (mediator or adapter)
	 * @param dstPort
	 *            the source element port (mediator or adapter); CAN be NULL
	 * @throws MetadataException
	 *             XML error
	 */
	protected void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort)
			throws MetadataException {

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
		Node componentNode = XMLUtil.getOrCreateSubNode(document, chainNode, "bindings");

		Element child = document.createElement("binding");
		child.setAttribute("from", from);
		child.setAttribute("to", to);
		componentNode.appendChild(child);

		// Write it back to file system
		writeDOM(document);

		// Notifies Repository
		DsciliaRepoService.getInstance().updateModel();
	}

	/**
	 * Deletes a binding in the XML document.
	 * 
	 * @param chain
	 *            the chain the binding belongs to
	 * @param from
	 *            source[:port]
	 * @param to
	 *            destination[:port]
	 * @throws MetadataException
	 *             XML error or binding not found
	 */
	protected void deleteBinding(Chain chain, String from, String to) throws MetadataException {

		Document document = getDocument();
		Node chainNode = findXMLChainNode(document, chain.getId());
		Node componentNode = XMLUtil.getOrCreateSubNode(document, chainNode, "bindings");

		Node[] target = XMLUtil.findXMLChildNode(componentNode, "binding", "from", from, "to", to);

		if (target.length != 0) {
			for (Node n : target) {
				componentNode.removeChild(n);
			}

			// Write it back to file system
			writeDOM(document);

			// Notifies Repository
			DsciliaRepoService.getInstance().updateModel();
		} else
			throw new MetadataException("Can't find binding from " + from + " to " + to);
	}

	/**
	 * Gets the chains.
	 * 
	 * @return the chains
	 */
	public List<Chain> getChains() {
		return chains;
	}

	/**
	 * Gets the file path.
	 * 
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int index = filePath.lastIndexOf(File.separator, filePath.length());
		if (index == -1)
			return filePath;
		else
			return filePath.substring(index + 1);
	}

	/**
	 * Merge another {@link Dscilia} into the current one. Differences between
	 * the argument and the current object are injected into the current object.
	 * 
	 * @param newInstance
	 *            an 'up-to-date' object
	 * @return a list of {@link Changeset}, which can be empty.
	 */
	protected Changeset[] merge(Dscilia newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<Chain> itr = chains.iterator(); itr.hasNext();) {
			Chain old = itr.next();
			String id = old.getId();
			Chain updated = MergeUtil.pullChain(newInstance, id);
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
