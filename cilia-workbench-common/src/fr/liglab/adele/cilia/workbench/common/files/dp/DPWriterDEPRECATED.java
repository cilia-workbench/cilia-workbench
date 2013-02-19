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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import fr.liglab.adele.cilia.workbench.common.files.FileUtil;
import fr.liglab.adele.cilia.workbench.common.files.StreamUtil;
import fr.liglab.adele.cilia.workbench.common.files.jar.JarReader;
import fr.liglab.adele.cilia.workbench.common.files.jar.JarWriter;

/**
 * DON'T USE THIS ! INSTEAD, USE
 * https://github.com/cescoffier/osgi-deployment-admin
 * /tree/master/deployment-package-api
 * 
 * @author Etienne Gandrille
 */
@Deprecated
public class DPWriterDEPRECATED {

	public static JarOutputStream createJar(String name, String version, List<String> filesPath) throws IOException {

		String manifest = getManifestContent(name, version, filesPath);
		JarWriter jarWriter = new JarWriter(manifest);

		for (String filePath : filesPath) {
			File fileInJar = new File(filePath);
			jarWriter.addFileInJar(fileInJar, DPContentManager.getFilePathInJar(fileInJar));
		}

		return jarWriter.getStream();
	}

	public static String getManifestContent(String name, String version, List<String> filesPath) throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(Attributes.Name.CONTENT_TYPE, "application/vnd.osgi.dp");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-ContactAddress"), "http://adele.imag.fr");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-DocURL"), "http://wikiadele.imag.fr/index.php/Cilia/cilia.dp");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-License"), "Apache License, Version 2.0");
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-SymbolicName"), name);
		manifest.getMainAttributes().put(new Attributes.Name("DeploymentPackage-Version"), version);

		Map<String, Attributes> entries = manifest.getEntries();
		for (String filePath : filesPath) {
			DPContentManager.addEnriesForFileInManifest(entries, new File(filePath));
		}
		OutputStream manifestStream = new ByteArrayOutputStream();
		manifest.write(manifestStream);
		String manifestContent = manifestStream.toString();
		manifestContent += "\r\n\r\n";
		manifestStream.close();

		return manifestContent;
	}

	private enum DPContentManager {

		DSCiliaFileManager("dscilia", "") {
			@Override
			protected void addFileInManifestInternal(Map<String, Attributes> entries, File source) throws IOException {
				String sha = FileUtil.sha1sum(source);
				Attributes attrs = new Attributes(2);
				attrs.putValue("Resource-Processor", "org.osgi.deployment.rp.autoload");
				attrs.putValue("SHA1-Digest", sha);
				entries.put(getName(source), attrs);
			}
		},

		JarFileManager("jar", "bundles/") {
			@Override
			protected void addFileInManifestInternal(Map<String, Attributes> entries, File source) throws IOException {
				// dump manifest to string
				InputStream is = JarReader.inputStreamFromFileInJarArchive(source, "META-INF/MANIFEST.MF");
				String jarContent = StreamUtil.streamToString(is);
				StreamUtil.closeStream(is);

				// manifest infos
				String bsn = readPropertyInManifest(jarContent, "Bundle-SymbolicName");
				String bv = readPropertyInManifest(jarContent, "Bundle-Version");
				String sha = FileUtil.sha1sum(source);

				Attributes attrs = new Attributes(3);
				attrs.putValue("Bundle-Version", bv);
				attrs.putValue("SHA1-Digest", sha);
				attrs.putValue("Bundle-SymbolicName", bsn);
				entries.put(getName(source), attrs);
			}

			private String readPropertyInManifest(String manifest, String property) throws IOException {
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
		};

		private final String ext;
		private final String filePathInJar;

		private DPContentManager(String ext, String filePathInJar) {
			this.ext = ext;
			this.filePathInJar = filePathInJar;
		}

		public static void addEnriesForFileInManifest(Map<String, Attributes> entries, File source) throws IOException {
			getManager(source.getPath()).addFileInManifestInternal(entries, source);
		}

		public String getFilePathInJar() {
			return filePathInJar;
		}

		public String getName(File source) {
			return getFilePathInJar() + source.getName();
		}

		public static String getFilePathInJar(File source) throws IOException {
			return getManager(source.getPath()).getName(source);
		}

		public static DPContentManager getManager(String filePath) {
			if (filePath == null)
				throw new IllegalArgumentException("file can't be null");

			for (DPContentManager manager : DPContentManager.values()) {
				if (filePath.endsWith("." + manager.ext))
					return manager;
			}

			throw new IllegalArgumentException("can't find resource manager for " + filePath);
		}

		protected abstract void addFileInManifestInternal(Map<String, Attributes> entries, File source) throws IOException;
	}
}
