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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * 
 * @author Etienne Gandrille
 */
public class JarWriter {

	private final JarOutputStream jos;

	public JarWriter(String manifest) throws IOException {
		OutputStream out = new FileOutputStream("/home/etienne/Bureau/MEDICAL/cilia-remote-distribution/file.dp");
		// OutputStream out = new ByteArrayOutputStream();

		jos = new JarOutputStream(out);
		addManifestInJar(manifest);
	}

	public JarWriter(Manifest manifest) throws IOException {
		OutputStream out = new FileOutputStream("/home/etienne/Bureau/MEDICAL/cilia-remote-distribution/file.dp");
		// OutputStream out = new ByteArrayOutputStream();
		jos = new JarOutputStream(out, manifest);
	}

	public JarOutputStream getStream() {
		return jos;
	}

	private void addManifestInJar(String manifest) throws IOException {
		addFileInJar(manifest, "META-INF/MANIFEST.MF");
	}

	public void addFileInJar(File source, String pathAndNameInJar) throws IOException {
		BufferedInputStream in = null;
		try {
			JarEntry entry = new JarEntry(pathAndNameInJar);
			entry.setTime(source.lastModified());
			jos.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer);
				if (count == -1)
					break;
				jos.write(buffer, 0, count);
			}
			jos.closeEntry();
		} finally {
			if (in != null)
				in.close();
		}
	}

	public void addFileInJar(String content, String pathAndNameInJar) throws IOException {
		JarEntry entry = new JarEntry(pathAndNameInJar);
		entry.setTime(System.currentTimeMillis());
		jos.putNextEntry(entry);
		jos.write(content.getBytes());
		jos.closeEntry();
	}
}
