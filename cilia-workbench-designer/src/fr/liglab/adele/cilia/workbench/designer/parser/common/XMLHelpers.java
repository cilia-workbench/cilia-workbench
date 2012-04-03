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
package fr.liglab.adele.cilia.workbench.designer.parser.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

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

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;

/**
 * Static methods, for managing XML files.
 * 
 * @author Etienne Gandrille
 */
public class XMLHelpers {

	/**
	 * Gets a {@link Document} from a {@link File}.
	 * 
	 * @param file
	 *            the file
	 * @return the dom document
	 * @throws MetadataException
	 *             if any error during parsing.
	 */
	public static Document getDocument(File file) throws MetadataException {
		try {
			return getDocumentBuilder().parse(file);
		} catch (SAXException e) {
			throw new MetadataException("Can't parse document from file " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new MetadataException("Can't parse document from file " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Gets a {@link Document} from an {@link InputStream}.
	 * 
	 * @param is
	 *            the input stream
	 * @return the dom document
	 * @throws MetadataException
	 *             if any error during parsing.
	 */
	public static Document getDocument(InputStream is) throws MetadataException {
		try {
			return getDocumentBuilder().parse(is);
		} catch (SAXException e) {
			throw new MetadataException("Can't parse document from stream", e);
		} catch (IOException e) {
			throw new MetadataException("Can't parse document from stream", e);
		}
	}

	/**
	 * Gets a document builder.
	 * 
	 * @return the document builder
	 * @throws MetadataException
	 *             if can't get builder
	 */
	private static DocumentBuilder getDocumentBuilder() throws MetadataException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new MetadataException("Can't get document builder ", e);
		}

		return builder;
	}

	/**
	 * Gets an Input stream from file embedded in a jar archive.
	 * 
	 * @param archivePath
	 *            the archive path, on the hard disk.
	 * @param fileName
	 *            the file name, in the archive. The file must be located at the archive root.
	 * @return the input stream
	 * @throws MetadataException
	 *             if any error.
	 */
	public static InputStream inputStreamFromFileInJarArchive(String archivePath, String fileName)
			throws MetadataException {
		// Jar file
		JarFile file;
		try {
			file = new JarFile(archivePath);
		} catch (IOException e) {
			throw new MetadataException("Can't open jar file " + archivePath, e);
		}

		// File
		ZipEntry entry = file.getEntry(fileName);
		if (entry == null)
			throw new MetadataException("File " + fileName + " not found in " + archivePath);

		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			throw new MetadataException("Can't access file " + fileName + " in jar file " + archivePath, e);
		}

		return is;
	}

	/**
	 * Gets the root node in a document.
	 * 
	 * @param document
	 *            the document
	 * @param nodeName
	 *            the node name
	 * @return the root node
	 * @throws MetadataException
	 *             if the root node doesn't match nodeName.
	 */
	public static Node getRootNode(Document document, String nodeName) throws MetadataException {
		NodeList nodes = document.getChildNodes();
		if (nodes != null && nodes.getLength() == 1 && nodes.item(0).getNodeName().equalsIgnoreCase(nodeName))
			return nodes.item(0);
		else
			throw new MetadataException("Can't find root node " + nodeName);
	}

	/**
	 * Writes a document using its DOM representation.
	 * 
	 * @param document
	 *            the document
	 * @param filePath
	 *            the file path, on the local file system.
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public static void writeDOM(Document document, String filePath) throws MetadataException {
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

	public static Node getOrCreateNode(Document document, Node parent, String nodeName) {

		Node dstNode = XMLReflectionUtil.findChild(parent, nodeName);
		if (dstNode == null) {
			dstNode = createNode(document, parent, nodeName);
		}
		return dstNode;
	}

	public static Node createNode(Document document, Node parent, String nodeName) {
		return createNodeInternal(document, parent, nodeName);
	}

	public static Node createNode(Document document, Node parent, String nodeName, String attrName, String attrValue) {
		return createNodeInternal(document, parent, nodeName, attrName, attrValue);
	}

	public static Node createNode(Document document, Node parent, String nodeName, String attrName1, String attrValue1,
			String attrName2, String attrValue2) {
		return createNodeInternal(document, parent, nodeName, attrName1, attrValue1, attrName2, attrValue2);
	}

	private static Node createNodeInternal(Document document, Node parent, String nodeName,
			String... attr_name_and_value) {
		Element newNode = document.createElement(nodeName);

		for (int i = 0; i < attr_name_and_value.length; i += 2) {
			String attrName = attr_name_and_value[i];
			String attrValue = attr_name_and_value[i + 1];
			newNode.setAttribute(attrName, attrValue);
		}
		parent.appendChild(newNode);
		return newNode;
	}
}
