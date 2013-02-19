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
package fr.liglab.adele.cilia.workbench.common.files.dp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import fr.liglab.adele.cilia.workbench.common.files.StreamUtil;
import fr.liglab.adele.cilia.workbench.common.files.jar.JarReader;
import fr.liglab.adele.cilia.workbench.common.files.jar.JarUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class DPReader {

	public static InputStream inputStreamFromFileInJarInDP(File jarFile, String subJar, String fileInSubJar) throws IOException {
		JarFile outerJarFile = null;
		InputStream in = null;
		File tempFile = null;
		FileOutputStream tempOut = null;
		InputStream retval = null;

		// Jar file
		outerJarFile = JarUtil.getJarFile(jarFile);

		// Entry in jar file
		ZipEntry outerZipEntry = outerJarFile.getEntry(subJar);
		if (outerZipEntry == null) {
			JarUtil.closeFile(outerJarFile);
			throw new IOException("File " + subJar + " not found in archive " + jarFile.getName());
		}
		JarEntry outerJarEntry = new JarEntry(outerZipEntry);

		Exception exception = null;
		try {
			in = outerJarFile.getInputStream(outerJarEntry);
			tempFile = File.createTempFile("tempFile", "jar");
			tempOut = new FileOutputStream(tempFile);
			StreamUtil.copyStream(in, tempOut, outerJarEntry);
			retval = JarReader.inputStreamFromFileInJarArchive(tempFile, fileInSubJar);
		} catch (Exception e) {
			exception = e;
		} finally {
			JarUtil.closeFile(outerJarFile);
			StreamUtil.closeStream(in);
			StreamUtil.closeStream(tempOut);
		}

		if (exception != null)
			throw new IOException(exception);

		return retval;
	}

	public static List<JarEntry> findJarInDP(File jarFile) {
		List<JarEntry> retval = new ArrayList<JarEntry>();

		JarFile file = null;
		try {
			file = new JarFile(jarFile);
			Enumeration<JarEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith("bundles/") && entry.getName().endsWith(".jar")) {
					retval.add(entry);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return retval;
	}

	public static boolean hasFile(File jarFile, String subJar, String fileInSubJar) {
		try {
			InputStream is = inputStreamFromFileInJarInDP(jarFile, subJar, fileInSubJar);
			if (is != null) {
				StreamUtil.closeStream(is);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
