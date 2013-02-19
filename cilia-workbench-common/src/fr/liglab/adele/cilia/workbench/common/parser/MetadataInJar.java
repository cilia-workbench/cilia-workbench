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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;

import fr.liglab.adele.cilia.workbench.common.files.jar.JarReader;

/**
 * 
 * @author Etienne Gandrille
 */
public class MetadataInJar extends PhysicalResource {

	private final File file;
	public final static String embeddedFileName = "metadata.xml";

	public MetadataInJar(File file) {
		this.file = file;
	}

	@Override
	public String getId() {
		return file.getPath();
	}

	@Override
	public String getDisplayName() {
		return file.getName();
	}

	@Override
	public File getExactResourceFile() throws InvalidObjectException {
		throw new InvalidObjectException("Can't get Java File from a zip file");
	}

	@Override
	public File getAssociatedResourceFile() {
		return file;
	}

	public InputStream getContentAsStream() throws IOException {
		return JarReader.inputStreamFromFileInJarArchive(file, embeddedFileName);
	}

	public boolean hasMetadata() {
		try {
			return JarReader.hasFile(file, embeddedFileName);
		} catch (IOException e) {
			return false;
		}
	}
}
