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
package fr.liglab.adele.cilia.workbench.common.parser.chain;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.element.Dispatcher;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.Processor;
import fr.liglab.adele.cilia.workbench.common.parser.element.Scheduler;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class MediatorRef extends ComponentRef {

	public MediatorRef(String id, NameNamespaceID referencedComponentID) {
		super(id, referencedComponentID);
	}

	public abstract Mediator getReferencedComponent();

	// PARAMETERS
	// ==========

	// Current parameters

	public abstract List<ParameterRef> getSchedulerParameters();

	public abstract List<ParameterRef> getProcessorParameters();

	public abstract List<ParameterRef> getDispatcherParameters();

	// Parameters definition, in referenced object

	public List<? extends ParameterDefinition> getReferencedComponentSchedulerParameters() {

		Mediator ro = getReferencedComponent();
		if (ro == null)
			return null;

		Scheduler part = ro.getScheduler();
		if (part == null)
			return null;

		return part.getParameters();
	}

	public List<? extends ParameterDefinition> getReferencedComponentProcessorParameters() {

		Mediator ro = getReferencedComponent();
		if (ro == null)
			return null;

		Processor part = ro.getProcessor();
		if (part == null)
			return null;

		return part.getParameters();
	}

	public List<? extends ParameterDefinition> getReferencedComponentDispatcherParameters() {

		Mediator ro = getReferencedComponent();
		if (ro == null)
			return null;

		Dispatcher part = ro.getDispatcher();
		if (part == null)
			return null;

		return part.getParameters();
	}

	// MISC
	// ====

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getIncommingBindings().length == 0)
			e1 = new CiliaError(this + " doesn't have an incomming binding", this);

		if (getOutgoingBindings().length == 0)
			e2 = new CiliaError(this + " doesn't have an outgoing binding", this);

		return CiliaFlag.generateTab(tab, e1, e2);
	}
}
