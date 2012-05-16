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
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * A property is a key value pair. This is a base class.
 * 
 * @author Etienne Gandrille
 */
public abstract class GenericProperty implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable {

	/** The key */
	protected String key;

	/** The value */
	protected String value;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Object getId() {
		return key;
	}

	@Override
	public String toString() {
		if (key == null || key.length() == 0)
			return "<undefined> = " + value;
		if (value == null || value.length() == 0)
			return key + " = <undefined>";

		return key + " = " + value;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		if (!(arg0 instanceof GenericProperty))
			return false;

		GenericProperty prop = (GenericProperty) arg0;

		if (prop.getKey() != key || prop.getValue() != value)
			return false;

		return true;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, key, "key");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, value, "value");

		return CiliaFlag.generateTab(e1, e2);
	}
}
