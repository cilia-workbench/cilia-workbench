package fr.liglab.adele.cilia.workbench.designer.parser.metadata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Bundle {

	private String filePath;
	private IPojo metadata;

	public Bundle(String filePath) throws Exception {
		this.filePath = filePath;

		InputStream is = inputStreamFromFile(filePath);
		
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur;
		try {
			constructeur = fabrique.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new MetadataException("Can't get document builder " + filePath, e);
		}

		Document document;
		try {
			document = constructeur.parse(is);
		} catch (SAXException e) {
			throw new MetadataException("Can't parse document " + filePath, e);
		} catch (IOException e) {
			throw new MetadataException("Can't parse document " + filePath, e);
		}

		// Parsing

		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase("ipojo"))
			metadata = new IPojo(nodes.item(0));
		else
			throw new MetadataException("Can't find ipojo root in " + filePath);
	}

	public static InputStream inputStreamFromFile(String filePath) throws MetadataException {
		// Jar file
		JarFile file;
		try {
			file = new JarFile(filePath);
		} catch (IOException e) {
			throw new MetadataException("Can't open jar file " + filePath, e);
		}

		// Metadata
		ZipEntry entry = file.getEntry("metadata.xml");
		if (entry == null)
			throw new MetadataException("Metadata not found in " + filePath);

		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			throw new MetadataException("Can't access metadata from file " + filePath, e);
		}

		return is;
	}
	
	public String getBundleName() {
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

	public IPojo getMetadata() {
		return metadata;
	}
}
