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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.common.XMLReflectionUtil;

/**
 * Represents a MediatorComponent.
 * 
 * @author Etienne Gandrille
 */
public class MediatorComponent {

	/** The name of the mediator component. */
	private String name;

	/** The category of the mediator component. */
	private String category;

	/** The scheduler name. */
	private String schedulerName;

	/** The processor name. */
	private String processorName;

	/** The dispatcher name. */
	private String dispatcherName;

	/** The ports. Can be in or out ports. */
	private List<Port> ports = new ArrayList<Port>();

	/**
	 * Instantiates a new MediatorComponent, using reflection on the DOM model.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws MetadataException
	 *             error while parsing the XML node.
	 */
	public MediatorComponent(Node node) throws MetadataException {

		XMLReflectionUtil.setRequiredAttribute(node, "name", this, "name");
		XMLReflectionUtil.setRequiredAttribute(node, "category", this, "category");

		Node schedulerNode = XMLReflectionUtil.findChild(node, "scheduler");
		if (schedulerNode == null)
			throw new MetadataException("scheduler element not found");
		XMLReflectionUtil.setRequiredAttribute(schedulerNode, "name", this, "schedulerName");

		Node processorNode = XMLReflectionUtil.findChild(node, "processor");
		if (processorNode == null)
			throw new MetadataException("processor element not found");
		XMLReflectionUtil.setRequiredAttribute(processorNode, "name", this, "processorName");

		Node dispatcherNode = XMLReflectionUtil.findChild(node, "dispatcher");
		if (dispatcherNode == null)
			throw new MetadataException("dispatcher element not found");
		XMLReflectionUtil.setRequiredAttribute(dispatcherNode, "name", this, "dispatcherName");

		Node portsNode = XMLReflectionUtil.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLReflectionUtil.findChildren(portsNode, "in-port");
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLReflectionUtil.findChildren(portsNode, "out-port");
			for (Node outPort : outPorts)
				ports.add(new OutPort(outPort));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Gets the ports.
	 * 
	 * @return the ports
	 */
	public List<Port> getPorts() {
		return ports;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
