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

import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;

/**
 * The Class Property.
 * 
 * @author Etienne Gandrille
 */
public class NameValueProperty extends GenericProperty {

	/** The value */
	protected String value;

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (name == null || name.length() == 0)
			return "<undefined> = " + value;
		if (value == null || value.length() == 0)
			return name + " = <undefined>";

		return name + " = " + value;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		if (!(arg0 instanceof GenericProperty))
			return false;

		NameValueProperty prop = (NameValueProperty) arg0;

		if (prop.getName() != name || prop.getValue() != value)
			return false;

		return true;
	}

	/**
	 * Instantiates a new property.
	 * 
	 * @param name
	 *            the key
	 * @param value
	 *            the value
	 */
	public NameValueProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		CiliaFlag e1 = CiliaWarning.checkStringNotNullOrEmpty(this, value, "value");

		return CiliaFlag.generateTab(tab, e1);
	}
}
