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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Element implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	private String name;
	private String classname;
	private String namespace;

	private List<Parameter> parameters = new ArrayList<Parameter>();

	public Element(Node node) throws MetadataException {
		XMLReflectionUtil.setAttribute(node, "name", this, "name");
		XMLReflectionUtil.setAttribute(node, "classname", this, "classname");
		XMLReflectionUtil.setAttribute(node, "namespace", this, "namespace");

		Node rootParam = XMLHelpers.findChild(node, "properties");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, "property");
			for (Node param : params)
				parameters.add(new Parameter(param));
		}
	}

	public String getName() {
		return name;
	}

	public String getClassname() {
		return classname;
	}

	public String getNamespace() {
		return namespace;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkNotNull(this, name, "name");
		CiliaFlag e2 = CiliaError.checkNotNull(this, classname, "class name");

		return CiliaFlag.generateTab(e1, e2);
	}
}
