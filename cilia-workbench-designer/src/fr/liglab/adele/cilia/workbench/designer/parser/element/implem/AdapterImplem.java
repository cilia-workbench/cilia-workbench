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
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.parser.element.AdapterDefinition;
import fr.liglab.adele.cilia.workbench.common.parser.element.IPort;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AdapterImplem extends AdapterDefinition {

	private PortsList ports;

	private String name;
	private String namespace;

	public AdapterImplem(Node node) throws CiliaException {
		ports = new PortsList(node);
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public NameNamespaceID getId() {
		return new NameNamespaceID(name, namespace);
	}

	@Override
	public String toString() {
		return name;
	}

	public List<IPort> getPorts() {
		return ports.getPorts();
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

	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, namespace, "namespace");
		CiliaFlag e3 = CiliaError.checkStringNotNullOrEmpty(this, getSubElement(), "sub element");

		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		return CiliaFlag.generateTab(flagsTab, e1, e2, e3);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}
}
