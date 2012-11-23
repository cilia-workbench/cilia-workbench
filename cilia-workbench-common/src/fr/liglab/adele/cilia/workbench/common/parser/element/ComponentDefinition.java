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
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.Port.PortNature;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * Describes component a type, which can be a specification or an
 * implementation.
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentDefinition implements Identifiable, DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	private final NameNamespaceID id;

	private final List<Port> ports;

	public ComponentDefinition(NameNamespaceID id, List<Port> ports) {
		this.id = id;
		this.ports = ports;
	}

	// ID AND VALUES
	// =============

	public NameNamespaceID getId() {
		return id;
	}

	public String getName() {
		return id.getName();
	}

	public String getNamespace() {
		return id.getNamespace();
	}

	// PORTS
	// =====

	public List<Port> getPorts() {
		return ports;
	}

	public List<InPort> getInPorts() {
		List<InPort> retval = new ArrayList<InPort>();
		for (Port p : getPorts())
			if (p.getNature() == PortNature.IN)
				retval.add((InPort) p);
		return retval;
	}

	public List<OutPort> getOutPorts() {
		List<OutPort> retval = new ArrayList<OutPort>();
		for (Port p : getPorts())
			if (p.getNature() == PortNature.OUT)
				retval.add((OutPort) p);
		return retval;
	}

	public boolean hasInPort(String name) {
		for (Port port : getInPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}

	public boolean hasOutPort(String name) {
		for (Port port : getOutPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}

	// COMPONENT NATURE
	// ================

	/**
	 * Ask the object nature : specification or implementation.
	 * 
	 * @return the object nature.
	 */
	public abstract ComponentNature getNature();

	public enum ComponentNature {

		SPEC("spec", "specification"), IMPLEM("implem", "implementation");

		private String shortName;
		private String longName;

		ComponentNature(String shortName, String longName) {
			this.shortName = shortName;
			this.longName = longName;
		}

		public String getShortName() {
			return shortName;
		}

		public String getLongName() {
			return longName;
		}
	}

	// MISC
	// ====

	@Override
	public String toString() {
		return Strings.nullToEmpty(id.getName());
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();

		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, getName(), "name");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, getNamespace(), "namespace");

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}
