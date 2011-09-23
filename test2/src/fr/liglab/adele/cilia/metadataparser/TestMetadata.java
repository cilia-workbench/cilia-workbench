package fr.liglab.adele.cilia.metadataparser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TestMetadata {

	public static void main(String[] args) throws Exception {

		//String name = "simple-test-0.0.1-SNAPSHOT.jar";
		String name = "hello-mediator-0.0.1-SNAPSHOT.jar";
		
		try {
			Bundle a = new Bundle(name);
		} catch (MetadataException e) {
			e.printStackTrace();
		}
		
	}

	private static void readFile() throws IOException, ParserConfigurationException, SAXException {

		String name = "simple-test-0.0.1-SNAPSHOT.jar";

		// JarEntry
		JarFile file = new JarFile(name);
		ZipEntry entry = file.getEntry("Seattle.xml");
		BufferedInputStream is = new BufferedInputStream(
				file.getInputStream(entry));

		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		Document document = constructeur.parse(is);
		
		Element element = document.getDocumentElement();
		System.out.println(element.getNodeName());
		is.close();

	}

	private static void writeFile()
			throws TransformerFactoryConfigurationError, TransformerException,
			ParserConfigurationException, IOException {

		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		Document document = constructeur.newDocument();

		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		Element racine = document.createElement("annuaire");
		racine.appendChild(document.createComment("Commentaire sous la racine"));
		document.appendChild(racine);

		Element personne = document.createElement("personne");
		personne.setAttribute("id", "0");
		racine.appendChild(personne);

		Element nom = document.createElement("nom");
		nom.setTextContent("un nom");
		personne.appendChild(nom);

		Element prenom = document.createElement("prenom");
		prenom.setTextContent("un prénom");
		personne.appendChild(prenom);

		Element adresse = document.createElement("adresse");
		adresse.setTextContent("une adresse");
		personne.appendChild(adresse);

		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		String stringResult = writer.toString();

		System.out.println(stringResult);

		//
		// // Source
		// Source source = new DOMSource(document);
		//
		// // Destination
		// StringWriter str = new StringWriter();
		// StreamResult result = new StreamResult(str);
		//
		// // Transformation
		// TransformerFactory transformerFactory =
		// TransformerFactory.newInstance();
		// Transformer xformer;
		// xformer = transformerFactory.newTransformer();
		// xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// xformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		// xformer.transform(source, result);
		//
		// // Writes to file system
		// //JMergeUtil.setContent(result_file, str.toString(), null);
		// System.out.println(result.toString());

	}
}
