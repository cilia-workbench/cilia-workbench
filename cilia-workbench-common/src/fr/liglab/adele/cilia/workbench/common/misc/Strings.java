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

/**
 * A few String methods, inspired from Guava Library.
 * 
 * @author Etienne Gandrille
 */
public class Strings {

	public static String nullToEmpty(String str) {
		return (str == null) ? "" : str;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
}
