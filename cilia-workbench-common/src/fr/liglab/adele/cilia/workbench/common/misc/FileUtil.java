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

package fr.liglab.adele.cilia.workbench.common.misc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * 
 * @author Etienne Gandrille
 */
public class FileUtil {

	public static File[] getFiles(File dir, final String extension) {
		if (dir == null)
			return new File[0];
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(extension);
			}
		});

		if (list == null)
			return new File[0];
		else
			return list;
	}

	public static void closeFile(ZipFile file) {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static JarFile getJarFile(File jarFile) throws CiliaException {
		try {
			return new JarFile(jarFile);
		} catch (IOException e) {
			throw new CiliaException("Can't open jar file " + jarFile.getAbsolutePath(), e);
		}
	}

	public static ZipEntry getJarEntry(JarFile file, String fileName) throws CiliaException {
		ZipEntry retval = file.getEntry(fileName);
		if (retval == null) {
			throw new CiliaException("File " + fileName + " not found in archive");
		}

		return retval;
	}
}
