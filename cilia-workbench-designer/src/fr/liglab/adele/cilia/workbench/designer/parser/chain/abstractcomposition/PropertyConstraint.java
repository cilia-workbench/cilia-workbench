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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.PropertyImplem;

/**
 * 
 * @author Etienne Gandrille
 */
public class PropertyConstraint implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_PROPERTY_CONSTRAINT = "property";
	public static String XML_ATTR_NAME = "name";
	public static String XML_ATTR_VALUE = "value";

	private final String name;
	private String value;

	public PropertyConstraint(Node n) throws CiliaException {
		name = XMLHelpers.findAttributeValue(n, XML_ATTR_NAME);
		value = XMLHelpers.findAttributeValue(n, XML_ATTR_VALUE);
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
		if (!(arg0 instanceof Property))
			return false;

		PropertyImplem prop = (PropertyImplem) arg0;

		if (prop.getName() != name || prop.getValue() != value)
			return false;

		return true;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		String newValue = ((PropertyConstraint) other).getValue();
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
