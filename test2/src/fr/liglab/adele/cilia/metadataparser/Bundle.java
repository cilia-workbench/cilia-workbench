package fr.liglab.adele.cilia.metadataparser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Bundle {

	private String bundleName;
	private IPojo metadata;
	
	
	
	public Bundle(String bundleName) throws Exception {
		this.bundleName = bundleName;
	
		// Jar file
		JarFile file;
		try {
			file = new JarFile(bundleName);
		} catch (IOException e) {
			throw new MetadataException("Can't open jar file " + bundleName, e);
		}
		
		// Metadata
		ZipEntry entry = file.getEntry("metadata.xml");
		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			throw new MetadataException("Can't access metadata from file " + bundleName, e);
		}
		
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur;
		try {
			constructeur = fabrique.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new MetadataException("Can't get document builder " + bundleName, e);
		}
		
		Document document;
		try {
			document = constructeur.parse(is);
		} catch (SAXException e) {
			throw new MetadataException("Can't parse document " + bundleName, e);
		} catch (IOException e) {
			throw new MetadataException("Can't parse document " + bundleName, e);
		}
		
		// Parsing
		
		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase("ipojo"))
			metadata = new IPojo(nodes.item(0));
		else
			throw new MetadataException("Can't find ipojo root in " + bundleName);
	}	
}
