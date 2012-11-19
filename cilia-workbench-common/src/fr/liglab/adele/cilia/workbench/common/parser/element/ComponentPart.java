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
package fr.liglab.adele.cilia.workbench.common.parser.element;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * Base interface for implementing IScheduler, IProcessor, IDispatcher.
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentPart implements ErrorsAndWarningsFinder, DisplayedInPropertiesView, Mergeable {

	protected final ParameterList parameters;

	public ComponentPart(ParameterList parameters) {
		this.parameters = parameters;
	}

	public List<ParameterDefinition> getParameters() {
		return parameters.getParameters();
	}

	public ParameterDefinition getParameter(String name) {
		return parameters.getParameter(name);
	}

	public List<Changeset> merge(Object newInstance) throws CiliaException {
		List<ParameterDefinition> newList = ((ComponentPart) newInstance).getParameters();
		return MergeUtil.mergeLists(newList, parameters.getParameters());
	}

	public CiliaFlag[] getErrorsAndWarnings() {
		return parameters.getErrorsAndWarnings();
	}
}
