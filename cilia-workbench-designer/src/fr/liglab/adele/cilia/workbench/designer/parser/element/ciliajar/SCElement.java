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
package fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class SCElement implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_CLASSNAME = "classname";

	private String name;
	private String classname;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public SCElement(Node node) throws CiliaException {
		parameters = Parameter.findParameters(node);
		ReflectionUtil.setAttribute(node, XML_ATTR_NAME, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_CLASSNAME, this, "classname");
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public Object getId() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> flagsTab = IdentifiableUtils.getErrorsNonUniqueId(this, parameters);

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, classname, "class name");

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}