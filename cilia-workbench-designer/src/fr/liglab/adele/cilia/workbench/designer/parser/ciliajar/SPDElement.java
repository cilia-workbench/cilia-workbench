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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IElement;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * Base class for implementing {@link Scheduler}, {@link Processor} and
 * {@link Dispatcher}.
 * 
 * @author Etienne Gandrille
 */
public abstract class SPDElement extends NameNamespace implements IElement, DisplayedInPropertiesView {

	private String classname;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public SPDElement(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, "name", this, "name");
		ReflectionUtil.setAttribute(node, "namespace", this, "namespace", CiliaConstants.getDefaultNamespace());
		parameters = Parameter.findParameters(node);
		ReflectionUtil.setAttribute(node, "classname", this, "classname");
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
