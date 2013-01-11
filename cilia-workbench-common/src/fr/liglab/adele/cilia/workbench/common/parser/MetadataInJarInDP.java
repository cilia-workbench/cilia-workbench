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
import java.io.InputStream;

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
		return FileUtil.inputStreamFromFileInJarInDP(jarFile, entry, MetadataInJar.embeddedFileName);
	}

	public boolean hasMetadata() {
		return FileUtil.hasFile(jarFile, entry, MetadataInJar.embeddedFileName);
	}

	@Override
	public boolean delete() {
		// Warning !
		// If delete needs to be implemented, don't forget a single dp file can
		// contain multiples sub jars...
		return false;
	}
}
