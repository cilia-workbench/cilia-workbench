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
package fr.liglab.adele.cilia.workbench.common.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.misc.FileUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class MetadataInJarInDP extends PhysicalResource {

	private final File jarFile;
	private final String entry;

	public MetadataInJarInDP(File jarFile, String entry) {
		this.jarFile = jarFile;
		this.entry = entry;
	}

	@Override
	public String getNameWithPath() {
		return jarFile.getPath() + ":" + entry;
	}

	@Override
	public String getName() {
		return jarFile.getName() + ":" + entry;
	}

	@Override
	public File getJavaFile() {
		throw new IllegalArgumentException("Can't get Java File from a zip file");
	}

	@Override
	public InputStream getContentAsStream() throws CiliaException {

		File tempFile = null;
		FileOutputStream tempOut = null;
		InputStream retval = null;

		// Jar file
		JarFile outerJarFile = FileUtil.getJarFile(jarFile);
		ZipEntry outerZipEntry = FileUtil.getJarEntry(outerJarFile, entry);

		JarEntry outerJarEntry = new JarEntry(outerZipEntry);
		try {
			InputStream in = outerJarFile.getInputStream(outerJarEntry);

			tempFile = File.createTempFile("tempFile", "jar");
			tempOut = new FileOutputStream(tempFile);

			copyStream(in, tempOut, outerJarEntry);

			retval = MetadataInJar.inputStreamFromFileInJarArchive(tempFile, MetadataInJar.embeddedFileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retval;
	}

	@Override
	public boolean delete() {
		// Warning !
		// If delete needs to be implemented, don't forget a single dp file can
		// contain multiples sub jars...
		return false;
	}

	private void copyStream(InputStream in, OutputStream out, JarEntry entry) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		long size = entry.getSize();
		while (-1 != (n = in.read(buffer)) && count < size) {
			out.write(buffer, 0, n);
			count += n;
		}
	}
}
