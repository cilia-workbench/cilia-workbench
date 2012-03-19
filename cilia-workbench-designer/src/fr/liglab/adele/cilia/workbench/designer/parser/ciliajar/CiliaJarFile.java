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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import java.io.BufferedInputStream;
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

import fr.liglab.adele.cilia.workbench.designer.parser.common.AbstractFile;

public class CiliaJarFile extends AbstractFile {

	private CiliaJarModel model;

	public CiliaJarFile(String filePath) throws Exception {
		super(filePath);

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
			model = new CiliaJarModel(nodes.item(0));
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

	public CiliaJarModel getModel() {
		return model;
	}
}
