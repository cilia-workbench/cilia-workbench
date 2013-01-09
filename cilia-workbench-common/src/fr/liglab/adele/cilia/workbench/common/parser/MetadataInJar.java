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
package fr.liglab.adele.cilia.workbench.common.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * 
 * @author Etienne Gandrille
 */
public class MetadataInJar extends PhysicalResource {

	private final File file;
	private final String embeddedFileName = "metadata.xml";

	public MetadataInJar(File file) {
		this.file = file;
	}

	@Override
	public Object getId() {
		return file.getPath();
	}

	@Override
	public String getFilename() {
		return file.getName();
	}

	@Override
	public File getJavaFile() {
		throw new IllegalArgumentException("Can't get Java File from a zip file");
	}

	public InputStream getContentAsStream() throws CiliaException {
		return inputStreamFromFileInJarArchive(file, embeddedFileName);
	}

	/**
	 * Gets an Input stream from a file embedded in a jar archive.
	 * 
	 * @param jarFile
	 *            the jar archive file, on the hard disk.
	 * @param fileName
	 *            the file name, in the archive. The file must be located at the
	 *            archive root.
	 * @return the input stream
	 * @throws CiliaException
	 *             if any error.
	 */
	public static InputStream inputStreamFromFileInJarArchive(File jarFile, String fileName) throws CiliaException {
		// Jar file
		JarFile file;
		try {
			file = new JarFile(jarFile);
		} catch (IOException e) {
			throw new CiliaException("Can't open jar file " + jarFile.getAbsolutePath(), e);
		}

		// File
		ZipEntry entry = file.getEntry(fileName);
		if (entry == null)
			throw new CiliaException("File " + fileName + " not found in " + jarFile.getAbsolutePath());

		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			throw new CiliaException("Can't access file " + fileName + " in jar file " + jarFile.getAbsolutePath(), e);
		}

		return is;
	}

	@Override
	public boolean delete() {
		try {
			return file.delete();
		} catch (Exception e) {
			return false;
		}
	}
}
