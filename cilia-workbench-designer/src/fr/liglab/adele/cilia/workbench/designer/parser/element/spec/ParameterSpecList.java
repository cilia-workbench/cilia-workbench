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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.GenericParameter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IComponentPart;

/**
 * 
 * @author Etienne Gandrille
 */
public class ParameterSpecList {

	private List<ParameterSpec> parameters;

	public ParameterSpecList() {
		parameters = new ArrayList<ParameterSpec>();
	}

	public ParameterSpecList(List<ParameterSpec> list) {
		parameters = list;
	}

	public ParameterSpecList(Node node) throws CiliaException {
		parameters = new ArrayList<ParameterSpec>();
		Node rootParam = XMLHelpers.findChild(node, "parameters");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, "parameter");
			for (Node param : params)
				parameters.add(new ParameterSpec(param));
		}
	}

	public void add(ParameterSpec parameterSpec) throws CiliaException {
		// no check. Done with getErrorsAndWarnings
		parameters.add(parameterSpec);
	}

	public List<ParameterSpec> getList() {
		return parameters;
	}

	public ParameterSpec getParameter(String name) {
		for (ParameterSpec p : parameters)
			if (p.getName().equalsIgnoreCase(name))
				return p;
		return null;
	}

	public CiliaFlag[] getErrorsAndWarnings() {
		return IdentifiableUtils.getErrorsNonUniqueId(this, parameters).toArray(new CiliaFlag[0]);
	}

	public List<Changeset> merge(IComponentPart newInstance) throws CiliaException {
		ArrayList<ParameterSpec> oldList = new ArrayList<ParameterSpec>();
		for (GenericParameter param : newInstance.getParameters()) {
			oldList.add((ParameterSpec) param);
		}

		return MergeUtil.mergeLists(oldList, parameters);
	}
}
