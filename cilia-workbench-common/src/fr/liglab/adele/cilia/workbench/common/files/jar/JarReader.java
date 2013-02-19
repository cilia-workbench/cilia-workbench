/**
 * Copyright 2012-2013 France Télécom 
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
package fr.liglab.adele.cilia.workbench.common.files.jar;

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
public class JarReader {

	/**
	 * Tests if a file exists in a jar file archive
	 * 
	 * @param jarFile
	 *            the jar archive file, on the hard disk.
	 * @param fileName
	 *            the file name, in the archive.
	 * @return true if the file exists
	 * @throws CiliaException
	 *             if the jar file can't be opened.
	 */
	public static boolean hasFile(File jarFile, String fileName) throws IOException {
		JarFile file = JarUtil.getJarFile(jarFile);
		ZipEntry entry = file.getEntry(fileName);
		boolean retval = (entry != null);
		JarUtil.closeFile(file);

		return retval;
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
	public static InputStream inputStreamFromFileInJarArchive(File jarFile, String fileName) throws IOException {

		// Jar file
		JarFile file = JarUtil.getJarFile(jarFile);

		// Entry
		ZipEntry entry = file.getEntry(fileName);
		if (entry == null) {
			JarUtil.closeFile(file);
			throw new IOException("File " + fileName + " not found in archive");
		}

		// Stream
		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			JarUtil.closeFile(file);
			throw new IOException("Can't access file " + fileName + " in jar file " + jarFile.getAbsolutePath(), e);
		}

		return is;
	}
}
