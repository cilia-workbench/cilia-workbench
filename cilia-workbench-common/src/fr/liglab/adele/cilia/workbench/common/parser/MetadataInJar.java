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
import java.io.InputStream;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.misc.FileUtil;

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
	public String getNameWithPath() {
		return file.getPath();
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public File getJavaFile() {
		throw new IllegalArgumentException("Can't get Java File from a zip file");
	}

	public InputStream getContentAsStream() throws CiliaException {
		return FileUtil.inputStreamFromFileInJarArchive(file, embeddedFileName);
	}

	public boolean hasMetadata() {
		try {
			return FileUtil.hasFile(file, embeddedFileName);
		} catch (CiliaException e) {
			return false;
		}
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
