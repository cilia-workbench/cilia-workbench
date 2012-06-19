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
package fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameValueProperty;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public class PropertyConstraint extends NameValueProperty implements Mergeable {

	public static final String XML_PROPERTY_CONSTRAINT = "property";
	public static String XML_ATTR_NAME = "name";
	public static String XML_ATTR_VALUE = "value";

	public PropertyConstraint(Node n) throws CiliaException {
		super(XMLHelpers.findAttributeValue(n, XML_ATTR_NAME), XMLHelpers.findAttributeValue(n, XML_ATTR_VALUE));
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
}
