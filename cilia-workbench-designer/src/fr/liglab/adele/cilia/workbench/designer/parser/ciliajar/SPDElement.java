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
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;

/**
 * 
 * @author Etienne Gandrille
 * 
 */
public abstract class SPDElement extends Element {

	private String classname;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public SPDElement(Node node) throws MetadataException {
		super(node);
		parameters = Parameter.findParameters(node);
		XMLReflectionUtil.setAttribute(node, "classname", this, "classname");
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] flags = super.getErrorsAndWarnings();

		List<CiliaFlag> flagsTab = IdentifiableUtils.getErrorsNonUniqueId(this, parameters);
		for (CiliaFlag flag : flags)
			flagsTab.add(flag);

		CiliaFlag e = CiliaError.checkStringNotNullOrEmpty(this, classname, "class name");

		return CiliaFlag.generateTab(flagsTab, e);
	}
}