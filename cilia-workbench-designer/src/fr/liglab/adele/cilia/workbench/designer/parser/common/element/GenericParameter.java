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
package fr.liglab.adele.cilia.workbench.designer.parser.common.element;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;

/**
 * Represents a parameter, used in Schedulers, Processors, Dispatchers.
 * 
 * @author Etienne Gandrille
 */
public class GenericParameter implements ErrorsAndWarningsFinder, Identifiable {

	/** Name */
	private String name;

	/** Default Value */
	private String default_value;

	@Override
	public Object getId() {
		return name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		String retval = name;

		if (default_value != null)
			retval = retval + " = " + default_value;

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");

		return CiliaFlag.generateTab(e1);
	}
}
