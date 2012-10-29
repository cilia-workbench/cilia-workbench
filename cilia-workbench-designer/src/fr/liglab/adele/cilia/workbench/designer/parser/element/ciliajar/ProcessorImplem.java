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

import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesViewWithForward;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IProcessor;

/**
 * 
 * @author Etienne Gandrille
 */
public class ProcessorImplem implements IProcessor, DisplayedInPropertiesViewWithForward, ErrorsAndWarningsFinder, Identifiable {

	public static final String XML_NODE_NAME = "processor";

	private final ComponentPartImplemHelper helper;

	public ProcessorImplem(Node node) throws CiliaException {
		helper = new ComponentPartImplemHelper(node);
	}

	public List<ParameterImplem> getParameters() {
		return helper.getParameters();
	}

	public ParameterImplem getParameter(String name) {
		return helper.getParameter(name);
	}

	public CiliaFlag[] getErrorsAndWarnings() {
		return helper.getErrorsAndWarnings();
	}

	@Override
	public Object getId() {
		return helper.getId();
	}

	@Override
	public String toString() {
		return helper.toString();
	}

	@Override
	public Object getObjectForComputingProperties() {
		return helper;
	}
}
