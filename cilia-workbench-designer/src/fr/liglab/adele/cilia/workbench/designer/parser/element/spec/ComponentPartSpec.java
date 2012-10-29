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
package fr.liglab.adele.cilia.workbench.designer.parser.element.spec;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IComponentPart;

/**
 * 
 * @author Etienne Gandrille
 */
abstract class ComponentPartSpec implements IComponentPart, DisplayedInPropertiesView, ErrorsAndWarningsFinder, Mergeable {

	private List<ParameterSpec> parameters = new ArrayList<ParameterSpec>();

	public ComponentPartSpec(Node node) throws CiliaException {

		Node rootParam = XMLHelpers.findChild(node, "parameters");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, "parameter");
			for (Node param : params)
				parameters.add(new ParameterSpec(param));
		}
	}

	public List<ParameterSpec> getParameters() {
		return parameters;
	}

	public ParameterSpec getParameter(String name) {
		for (ParameterSpec p : parameters)
			if (p.getName().equalsIgnoreCase(name))
				return p;
		return null;
	}

	public List<Changeset> merge(Object newInstance) throws CiliaException {
		return MergeUtil.mergeLists(((ComponentPartSpec) newInstance).getParameters(), parameters);
	}

	@Override
	public String toString() {
		String className = this.getClass().getName();
		int idx = className.lastIndexOf(".");
		if (idx == -1)
			return className;
		else
			return className.substring(idx + 1);
	}

	public static Node createXMLParameter(Document document, Node mediatorSpec, String param, String componentName) {
		Node component = XMLHelpers.getOrCreateChild(document, mediatorSpec, componentName);
		Node parameters = XMLHelpers.getOrCreateChild(document, component, "parameters");
		return XMLHelpers.createChild(document, parameters, "parameter", "name", param);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		return IdentifiableUtils.getErrorsNonUniqueId(this, parameters).toArray(new CiliaFlag[0]);
	}
}
