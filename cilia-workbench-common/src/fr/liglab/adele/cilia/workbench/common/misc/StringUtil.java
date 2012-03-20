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
 *
 * @author Etienne Gandrille
 */
public class StringUtil {

	private static final String SEPARATOR = ":";

	
	public static String getBeforeSeparatorOrAll(String name) {
		Preconditions.checkNotNull(name);

		int index = name.indexOf(SEPARATOR);
		if (index == -1)
			return name;
		return name.substring(0, index);
	}

	public static String getBeforeSeparatorOrNothing(String name) {
		Preconditions.checkNotNull(name);

		int index = name.indexOf(SEPARATOR);
		if (index == -1 || name.startsWith(SEPARATOR))
			return "";
		else
			return name.substring(0, index);
	}

	public static String getAfterSeparatorOrAll(String name) {
		Preconditions.checkNotNull(name);
		
		int index = name.indexOf(SEPARATOR);
		if (index == -1)
			return name;
		else
			return name.substring(index + 1);
	}

	
	public static String getFileName(String path) {
		Preconditions.checkNotNull(path);

		int index = path.lastIndexOf(File.separator, path.length());
		if (index == -1)
			return path;
		else
			return path.substring(index + 1);
	}
}
