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
import java.util.Map;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorComponent extends Element {

	private String schedulerName;
	private String schedulerNamespace;
	private String processorName;
	private String processorNamespace;
	private String dispatcherName;
	private String dispatcherNamespace;
	private List<Port> ports = new ArrayList<Port>();
	private List<Property> properties = new ArrayList<Property>();

	public MediatorComponent(Node node) throws MetadataException {
		super(node);

		String defNs = CiliaConstants.getDefaultNamespace();

		Map<String, String> attrMap = XMLHelpers.findAttributesValues(node);
		for (String attr : attrMap.keySet()) {
			if (!attr.equalsIgnoreCase("name") && !attr.equalsIgnoreCase("namespace"))
				properties.add(new Property(attr, attrMap.get(attr)));
		}

		Node schedulerNode = XMLHelpers.findChild(node, "scheduler");
		if (schedulerNode != null) {
			XMLReflectionUtil.setAttribute(schedulerNode, "name", this, "schedulerName");
			XMLReflectionUtil.setAttribute(schedulerNode, "namespace", this, "schedulerNamespace", defNs);
		}

		Node processorNode = XMLHelpers.findChild(node, "processor");
		if (processorNode != null) {
			XMLReflectionUtil.setAttribute(processorNode, "name", this, "processorName");
			XMLReflectionUtil.setAttribute(processorNode, "namespace", this, "processorNamespace", defNs);
		}

		Node dispatcherNode = XMLHelpers.findChild(node, "dispatcher");
		if (dispatcherNode != null) {
			XMLReflectionUtil.setAttribute(dispatcherNode, "name", this, "dispatcherName", defNs);
			XMLReflectionUtil.setAttribute(dispatcherNode, "namespace", this, "dispatcherNamespace");
		}

		Node portsNode = XMLHelpers.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLHelpers.findChildren(portsNode, "in-port");
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLHelpers.findChildren(portsNode, "out-port");
			for (Node outPort : outPorts)
				ports.add(new OutPort(outPort));
		}
	}

	public List<Port> getPorts() {
		return ports;
	}

	public List<Port> getInPorts() {
		List<Port> retval = new ArrayList<Port>();
		for (Port p : ports)
			if (p.isInPort())
				retval.add(p);
		return retval;
	}

	public List<Port> getOutPorts() {
		List<Port> retval = new ArrayList<Port>();
		for (Port p : ports)
			if (p.isOutPort())
				retval.add(p);
		return retval;
	}

	public NameNamespaceID getSchedulerNameNamespace() {
		return new NameNamespaceID(schedulerName, schedulerNamespace);
	}

	public NameNamespaceID getProcessorNameNamespace() {
		return new NameNamespaceID(processorName, processorNamespace);
	}

	public NameNamespaceID getDispatcherNameNamespace() {
		return new NameNamespaceID(dispatcherName, dispatcherNamespace);
	}

	public List<Property> getProperties() {
		return properties;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();
		for (CiliaFlag f : tab)
			flagsTab.add(f);

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, schedulerName, "scheduler name");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, processorName, "processor name");
		CiliaFlag e3 = CiliaError.checkStringNotNullOrEmpty(this, dispatcherName, "dispatcher name");
		CiliaFlag e4 = null;
		CiliaFlag e5 = null;

		// ports
		if (getInPorts().size() == 0)
			e4 = new CiliaError("Mediator doesn't have an in port", this);
		if (getOutPorts().size() == 0)
			e5 = new CiliaError("Mediator doesn't have an out port", this);
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		// properties
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, properties));

		return CiliaFlag.generateTab(flagsTab, e1, e2, e3, e4, e5);
	}
}
