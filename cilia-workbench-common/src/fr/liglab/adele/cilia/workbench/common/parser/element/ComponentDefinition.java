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

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.parser.element.IPort.PortNature;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentDefinition implements Identifiable, ComponentNatureAskable, DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	// PORTS
	// =====

	public abstract List<? extends IPort> getPorts();

	public List<InPort> getInPorts() {
		List<InPort> retval = new ArrayList<InPort>();
		for (IPort p : getPorts())
			if (p.getNature() == PortNature.IN)
				retval.add((InPort) p);
		return retval;
	}

	public List<OutPort> getOutPorts() {
		List<OutPort> retval = new ArrayList<OutPort>();
		for (IPort p : getPorts())
			if (p.getNature() == PortNature.OUT)
				retval.add((OutPort) p);
		return retval;
	}

	public boolean hasInPort(String name) {
		for (IPort port : getInPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}

	public boolean hasOutPort(String name) {
		for (IPort port : getOutPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}
}
