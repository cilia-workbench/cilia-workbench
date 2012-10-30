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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.Parameter;

/**
 * 
 * @author Etienne Gandrille
 */
public class ParameterImplem extends Parameter {

	public static final String XML_NODE_NAME = "property";

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_VALUE = "value";
	public static final String XML_ATTR_FIELD = "field";

	private String method;
	private String field;
	private String default_value;

	public ParameterImplem(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_NAME, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_METHOD, this, "method");
		ReflectionUtil.setAttribute(node, XML_ATTR_VALUE, this, "default_value");
		ReflectionUtil.setAttribute(node, XML_ATTR_FIELD, this, "field");
	}

	public static List<ParameterImplem> findParameters(Node node) throws CiliaException {

		List<ParameterImplem> retval = new ArrayList<ParameterImplem>();

		Node rootParam = XMLHelpers.findChild(node, "properties");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, XML_NODE_NAME);
			for (Node param : params)
				retval.add(new ParameterImplem(param));
		}

		return retval;
	}

	public boolean hasDefaultValue() {
		return !Strings.isNullOrEmpty(default_value);
	}

	@Override
	public String toString() {
		String retval = super.toString();

		if (default_value != null)
			retval = retval + " = " + default_value;

		if (method != null)
			retval = retval + " [" + method + "]";
		if (field != null)
			retval = retval + " [" + field + "]";

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		CiliaFlag e = null;

		if (method != null && field != null)
			e = new CiliaError("method and field parameters are excusive", this);

		return CiliaFlag.generateTab(tab, e);
	}
}
