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
package fr.liglab.adele.cilia.workbench.common.marker;

/**
 * This interface is implemented by objects able to generate errors and
 * warnings.
 * 
 * @author Etienne Gandrille
 */
public interface ErrorsAndWarningsFinder {

	/**
	 * Gets all errors and warnings relative to the current element.
	 * 
	 * @return an array, which can be empty, but must NOT be null.
	 */
	public CiliaFlag[] getErrorsAndWarnings();
}
