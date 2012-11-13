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
package fr.liglab.adele.cilia.workbench.designer.parser.element.implem;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IPort.PortNature;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.InPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.OutPort;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AdapterImplem extends NameNamespace implements IAdapter, Identifiable, ErrorsAndWarningsFinder {

	private List<IPort> ports;

	public AdapterImplem(Node node) throws CiliaException {
		ports = ComponentImplemHelper.getPorts(node);
	}

	public List<IPort> getPorts() {
		return ports;
	}

	public List<InPort> getInPorts() {
		List<InPort> retval = new ArrayList<InPort>();
		for (IPort p : ports)
			if (p.getNature() == PortNature.IN)
				retval.add((InPort) p);
		return retval;
	}

	public List<OutPort> getOutPorts() {
		List<OutPort> retval = new ArrayList<OutPort>();
		for (IPort p : ports)
			if (p.getNature() == PortNature.OUT)
				retval.add((OutPort) p);
		return retval;
	}

	@Override
	public boolean hasInPort(String name) {
		for (IPort port : getInPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}

	@Override
	public boolean hasOutPort(String name) {
		for (IPort port : getOutPorts())
			if (port.getName().equalsIgnoreCase(name))
				return true;

		return false;
	}

	/**
	 * Sub element is a collector or a sender, depending on the
	 * {@link AdapterType}.
	 * 
	 * @param subElement
	 *            the new sub element
	 */
	protected abstract void setSubElement(String subElement);

	/**
	 * Sub element is a collector or a sender, depending on the
	 * {@link AdapterType}.
	 * 
	 * @return the sub element
	 */
	protected abstract String getSubElement();

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();
		for (CiliaFlag f : tab)
			flagsTab.add(f);

		CiliaError e1 = CiliaError.checkStringNotNullOrEmpty(this, getSubElement(), "sub element");

		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		return CiliaFlag.generateTab(flagsTab, e1);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}
}
