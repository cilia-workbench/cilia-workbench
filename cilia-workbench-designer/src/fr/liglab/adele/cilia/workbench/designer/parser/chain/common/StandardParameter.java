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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.NameValueProperty;

/**
 * 
 * @author Etienne Gandrille
 */
public class StandardParameter extends NameValueProperty implements Mergeable {

	public static final String XML_ROOT_NAME = "property";
	public static String XML_ATTR_NAME = "name";
	public static String XML_ATTR_VALUE = "value";

	public StandardParameter(Node n) throws CiliaException {
		super(XMLHelpers.findAttributeValue(n, XML_ATTR_NAME), XMLHelpers.findAttributeValue(n, XML_ATTR_VALUE));
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		String newValue = ((StandardParameter) other).getValue();
		if (!value.equals(newValue)) {
			value = newValue;
			retval.add(new Changeset(Operation.UPDATE, this));
		}

		return retval;
	}
}
