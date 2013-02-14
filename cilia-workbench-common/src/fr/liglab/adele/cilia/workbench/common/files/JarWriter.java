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
package fr.liglab.adele.cilia.workbench.common.files;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * 
 * @author Etienne Gandrille
 */
public class JarWriter {

	// TODO remove this (for test purpose)
	public static void main(String[] args) {
		try {

			String path = "/home/etienne/Bureau/bundles/";
			List<String> jarPath = new ArrayList<String>();
			jarPath.add(path + "cilia-core-1.6.1-SNAPSHOT.jar");
			jarPath.add(path + "cilia-runtime-1.6.1-SNAPSHOT.jar");

			JarOutputStream stream = createJar("myJar", "v0.0.1", jarPath);

			System.out.println("end");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JarOutputStream createJar(String name, String version, List<String> jarPath) throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(Attributes.Name.CONTENT_TYPE, "application/vnd.osgi.dp");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-ContactAddress"), "http://adele.imag.fr");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-DocURL"), "http://wikiadele.imag.fr/index.php/Cilia/cilia.dp");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-License"), "Apache License, Version 2.0");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-SymbolicName"), name);
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-Version"), version);

		Map<String, Attributes> entries = manifest.getEntries();
		for (String filePath : jarPath) {
			addFileInManifest(entries, new File(filePath), "bundles/");
		}

		JarOutputStream target = new JarOutputStream(new ByteArrayOutputStream(), manifest);

		for (String filePath : jarPath) {
			addFileInJar(target, new File(filePath), "bundles/");
		}

		target.close();
		return target;
	}

	private static void addFileInJar(JarOutputStream target, File source, String jarPath) throws IOException {
		BufferedInputStream in = null;
		try {
			JarEntry entry = new JarEntry(jarPath + source.getName());
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		} finally {
			if (in != null)
				in.close();
		}
	}

	private static void addFileInManifest(Map<String, Attributes> entries, File source, String jarPath) throws IOException {
		// dump manifest to string
		InputStream is = JarReader.inputStreamFromFileInJarArchive(source, "META-INF/MANIFEST.MF");
		String content = StreamUtil.streamToString(is);
		StreamUtil.closeStream(is);

		// manifest infos
		String bsn = readPropertyInManifest(content, "Bundle-SymbolicName");
		String bv = readPropertyInManifest(content, "Bundle-Version");
		String sha = Sha1Util.sha1sum(source);

		String name = jarPath + source.getName();
		Attributes attrs = new Attributes(3);
		attrs.putValue("Bundle-Version", bv);
		attrs.putValue("SHA1-Digest", sha);
		attrs.putValue("Bundle-SymbolicName", bsn);
		entries.put(name, attrs);
	}

	private static String readPropertyInManifest(String manifest, String property) throws IOException {
		int start = manifest.indexOf(property);
		if (start == -1)
			throw new IOException("property " + property + " not found in manifest");
		int beginProperty = manifest.indexOf(":", start + property.length()) + 1;

		int endProperty = -1;
		int endProperty1 = manifest.indexOf("\r", beginProperty + 1);
		int endProperty2 = manifest.indexOf("\n", beginProperty + 1);

		if (endProperty1 != -1 && endProperty2 != -1) {
			endProperty = Math.min(endProperty1, endProperty2);
		} else if (endProperty1 != -1) {
			endProperty = endProperty1;
		} else if (endProperty2 != -1) {
			endProperty = endProperty2;
		}

		String value;
		if (endProperty == -1)
			value = manifest.substring(beginProperty);
		else
			value = manifest.substring(beginProperty, endProperty);

		return value.trim();
	}
}
