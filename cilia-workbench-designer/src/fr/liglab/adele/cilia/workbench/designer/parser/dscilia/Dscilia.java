package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

public class Dscilia {
	private String filePath;
	private List<Chain> chains = new ArrayList<Chain>();

	public Dscilia(String filePath) throws Exception {
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

	private Node getCiliaNode(Document document) throws MetadataException {
		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase("cilia"))
			return nodes.item(0);
		else
			throw new MetadataException("Can't find cilia root in " + filePath);
	}

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

	public void createChain(String chainName) throws MetadataException {

		// Document creation
		Document document = getDocument();
		Node root = getCiliaNode(document);
		Element child = document.createElement("chain");
		child.setAttribute("id", chainName);
		root.appendChild(child);

		// Write it back to file system
		writeDOM(document);
	}

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

	public void deleteChain(String id) throws MetadataException {
		// Finding target node
		Document document = getDocument();
		Node root = getCiliaNode(document);
		NodeList list = root.getChildNodes();
		Node target = null;
		for (int i=0; i<list.getLength() && target == null; i++) {
			Node current = list.item(i);
			if (current.getNodeName().equalsIgnoreCase("chain")) {
				NamedNodeMap attrs = current.getAttributes();
				for (int j=0; j<attrs.getLength() && target == null; j++) {
					Node attr = attrs.item(j);
					String name = attr.getNodeName();
					String value = attr.getNodeValue();
					if (name.equalsIgnoreCase("id") && value.equals(id))
						target = current;
				}
					
			}
		}
		
		if (target != null) {
			root.removeChild(target);
			writeDOM(document);
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

	public Changeset[] merge(Dscilia newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<Chain> itr = chains.iterator(); itr.hasNext();) {
			Chain old = itr.next();
			String id = old.getId();
			Chain updated = pullChain(newInstance, id);
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

		return retval.toArray(new Changeset[0]);
	}

	private Chain pullChain(Dscilia newInstance, String id) {
		for (Iterator<Chain> itr = newInstance.getChains().iterator(); itr.hasNext();) {
			Chain element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
