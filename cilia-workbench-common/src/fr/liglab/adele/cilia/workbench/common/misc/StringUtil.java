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
 * A small class for managing Strings.
 */
public class StringUtil {

	/** The SEPARATOR character. */
	private static final String SEPARATOR = ":";

	/**
	 * Gets the text before the separator, or all the text if the separator
	 * can't be found.
	 * 
	 * @param string
	 *            the text
	 * @return the text before the separator, or all the text if the separator
	 *         can't be found.
	 */
	public static String getBeforeSeparatorOrAll(String string) {
		Preconditions.checkNotNull(string);

		int index = string.indexOf(SEPARATOR);
		if (index == -1)
			return string;
		return string.substring(0, index);
	}

	/**
	 * Gets the text before the separator, or an empty string if the separator
	 * can't be found.
	 * 
	 * @param string
	 *            the text
	 * @return the text before the separator, or an empty string if the
	 *         separator can't be found.
	 */
	public static String getBeforeSeparatorOrNothing(String string) {
		Preconditions.checkNotNull(string);

		int index = string.indexOf(SEPARATOR);
		if (index == -1 || string.startsWith(SEPARATOR))
			return "";
		else
			return string.substring(0, index);
	}

	/**
	 * Gets the text after the separator, or all the text if the separator can't
	 * be found.
	 * 
	 * @param string
	 *            the text
	 * @return the text after the separator, or all the text if the separator
	 *         can't be found.
	 */
	public static String getAfterSeparatorOrAll(String string) {
		Preconditions.checkNotNull(string);

		int index = string.indexOf(SEPARATOR);
		if (index == -1)
			return string;
		else
			return string.substring(index + 1);
	}

	/**
	 * Gets the text after the separator, or an empty string if the separator
	 * can't be found.
	 * 
	 * @param string
	 *            the text
	 * @return the text after the separator, or an empty string if the separator
	 *         can't be found.
	 */
	public static String getAfterSeparatorOrNothing(String string) {
		Preconditions.checkNotNull(string);

		int index = string.indexOf(SEPARATOR);
		if (index == -1)
			return "";
		else
			return string.substring(index + 1);
	}

	/**
	 * Extract the file name form a complete path.
	 * 
	 * @param path
	 *            the path
	 * @return the file name
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
