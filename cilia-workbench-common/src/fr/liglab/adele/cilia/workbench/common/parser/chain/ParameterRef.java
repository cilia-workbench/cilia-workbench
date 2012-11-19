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
package fr.liglab.adele.cilia.workbench.common.parser.chain;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.parser.element.Property;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * A parameterRef "points" to a ParameterDefinition.
 * 
 * @author Etienne Gandrille
 */
public abstract class ParameterRef implements Mergeable, ErrorsAndWarningsFinder, Identifiable, DisplayedInPropertiesView {

	protected String name;
	protected String value;

	public ParameterRef(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public Object getId() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// name
		if (name == null || name.length() == 0)
			sb.append("<undefined>");
		else
			sb.append(name);

		// separator
		sb.append(" = ");

		// value
		if (value == null || value.length() == 0)
			sb.append("<undefined>");
		else
			sb.append(value);

		return sb.toString();
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		if (!(arg0 instanceof Property))
			return false;

		ParameterRef param = (ParameterRef) arg0;

		if (!param.getName().equals(name) || !param.getValue().equals(value))
			return false;

		return true;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		String newValue = ((ParameterRef) other).getValue();
		if (!value.equals(newValue)) {
			value = newValue;
			retval.add(new Changeset(Operation.UPDATE, this));
		}

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, value, "value");

		return CiliaFlag.generateTab(e1, e2);
	}
}
