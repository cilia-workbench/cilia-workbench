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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

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

import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;

/**
 * Indirection layer to {@link IPojo}.
 * 
 * While IPojo represents ONLY valid files (parsed successfully), this class
 * represents valid and non valid jar metadata. Non valid files have the
 * {@link #metadata} field null.
 * 
 * @author Etienne Gandrille
 */
public class Bundle {

	/** The path on the file system. */
	private String filePath;

	/** The logical representation of the jar metadata. */
	private IPojo metadata;

	/**
	 * Instantiates a new bundle.
	 * 
	 * @param filePath
	 *            the file path
	 * @throws Exception
	 *             the exception
	 */
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

	/**
	 * Gets an XML stream from a jar file.
	 * 
	 * @param filePath
	 *            the file path
	 * @return the input stream
	 * @throws MetadataException
	 *             the metadata exception
	 */
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

	/**
	 * Gets the bundle path.
	 * 
	 * @return the bundle path
	 */
	public String getBundlePath() {
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
	 * Gets the metadata.
	 * 
	 * @return the metadata
	 */
	public IPojo getMetadata() {
		return metadata;
	}
}
