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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentPart implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Mergeable {

	private List<Parameter> parameters = new ArrayList<Parameter>();

	public ComponentPart(Node node) throws CiliaException {

		Node rootParam = XMLHelpers.findChild(node, "parameters");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, "parameter");
			for (Node param : params)
				parameters.add(new Parameter(param));
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<Changeset> merge(Object newInstance) throws CiliaException {
		return MergeUtil.mergeLists(((ComponentPart) newInstance).getParameters(), parameters);
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
		Node component = XMLHelpers.getOrCreateNode(document, mediatorSpec, componentName);
		Node parameters = XMLHelpers.getOrCreateNode(document, component, "parameters");
		return XMLHelpers.createNode(document, parameters, "parameter", "name", param);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		return IdentifiableUtils.getErrorsNonUniqueId(this, parameters).toArray(new CiliaFlag[0]);
	}
}
