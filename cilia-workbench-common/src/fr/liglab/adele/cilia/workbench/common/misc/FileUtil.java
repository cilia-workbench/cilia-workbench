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

package fr.liglab.adele.cilia.workbench.common.misc;

import java.io.File;

import com.google.common.base.Preconditions;

/**
 * File utils.
 * 
 * @author Etienne Gandrille
 */
public class FileUtil {

	/**
	 * Returns the file name, without the path.
	 * 
	 * @param a
	 *            path to a file
	 * @return only the file part.
	 */
	public static String getFileName(String path) {
		Preconditions.checkNotNull(path);

		int index = path.lastIndexOf(File.separator, path.length());
		if (index == -1)
			return path;
		else
			return path.substring(index + 1);
	}
}