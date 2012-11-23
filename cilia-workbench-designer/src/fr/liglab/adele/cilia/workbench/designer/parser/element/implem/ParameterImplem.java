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
package fr.liglab.adele.cilia.workbench.designer.parser.element.implem;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class ParameterImplem extends ParameterDefinition {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_FIELD = "field";
	public static final String XML_ATTR_VALUE = "value";

	private final String method;
	private final String field;
	private final String default_value;

	public ParameterImplem(Node node) throws CiliaException {
		super(XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_NAME));

		method = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_METHOD);
		field = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_FIELD);
		default_value = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_VALUE);
	}

	public boolean hasDefaultValue() {
		return !Strings.isNullOrEmpty(default_value);
	}

	@Override
	public String toString() {
		String retval = super.toString();

		if (!Strings.isNullOrEmpty(default_value))
			retval = retval + " = " + default_value;

		if (!Strings.isNullOrEmpty(method))
			retval = retval + " [" + method + "]";
		if (!Strings.isNullOrEmpty(field))
			retval = retval + " [" + field + "]";

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		CiliaFlag e = null;

		if (!Strings.isNullOrEmpty(method) && !Strings.isNullOrEmpty(field))
			e = new CiliaError("method and field parameters are excusive", this);

		return CiliaFlag.generateTab(tab, e);
	}
}
