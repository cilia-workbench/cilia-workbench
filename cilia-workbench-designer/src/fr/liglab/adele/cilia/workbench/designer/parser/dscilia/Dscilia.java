package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class Dscilia {
	private String filePath;
	private List<Chain> chains = new ArrayList<Chain>();
	
	public Dscilia(String filePath) throws Exception {
		this.filePath = filePath;
		
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

		// Parsing
		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase("cilia")) {
			Node root = nodes.item(0);
			
			NodeList elements = root.getChildNodes();
			for (int i=0; i< elements.getLength(); i++) {
				Node child = elements.item(i);
					
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					String nodeName = child.getNodeName().toLowerCase();
				
					if (nodeName.equals("chain"))
						chains.add(new Chain(child));
				}
			}
		}
		else
			throw new MetadataException("Can't find cilia root in " + filePath);
	}
	
	public List<Chain> getChains() {
		return chains;
	}
	
	@Override
	public String toString() {
		int index = filePath.lastIndexOf(File.separator, filePath.length());
		if (index == -1)
			return filePath;
		else
			return filePath.substring(index + 1);
	}
}
