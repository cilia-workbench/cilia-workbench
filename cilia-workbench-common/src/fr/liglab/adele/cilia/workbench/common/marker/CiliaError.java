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

import org.eclipse.core.resources.IMarker;

/**
 * 
 * @author Etienne Gandrille
 */
public class CiliaError extends CiliaFlag {

	public CiliaError(String message, Object sourceProvider) {
		super(IMarker.SEVERITY_ERROR, message, sourceProvider);
	}

	public static CiliaError checkStringNotNullOrEmpty(Object sourceProvider, String string, String stringName) {
		if (string == null || string.length() == 0) {
			String message = stringName + " can't be null or empty";
			return new CiliaError(message, sourceProvider);
		} else
			return null;
	}

	public static CiliaError checkNotNull(Object sourceProvider, Object object, String objectName) {

		if (object == null) {
			String message = objectName + " can't be null";
			return new CiliaError(message, sourceProvider);
		} else
			return null;
	}
}
