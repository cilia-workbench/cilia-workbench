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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Port.PortType;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpec {

	public static final String XML_NODE_NAME = "mediator-specification";

	public static final String XML_NODE_PROPERTIES_CONTAINER = "properties";
	public static final String XML_NODE_PORTS_CONTAINER = "ports";

	private String id;
	public static final String XML_ATTR_ID = "id";
	private String namespace;
	public static final String XML_ATTR_NAMESPACE = "namespace";

	private List<Port> ports = new ArrayList<Port>();
	private List<Property> properties = new ArrayList<Property>();
	private Processor processor = null;
	private Scheduler scheduler = null;
	private Dispatcher dispatcher = null;
	private Node node;

	public MediatorSpec(Node node) throws MetadataException {
		this.node = node;
		XMLReflectionUtil.setRequiredAttribute(node, XML_ATTR_ID, this, "id");
		XMLReflectionUtil.setOptionalAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");

		Node rootPorts = XMLHelpers.findChild(node, XML_NODE_PORTS_CONTAINER);
		if (rootPorts != null) {
			Node[] ips = XMLHelpers.findChildren(rootPorts, PortType.IN.getXMLtag());
			for (Node ip : ips)
				ports.add(new InPort(ip));
			Node[] ops = XMLHelpers.findChildren(rootPorts, PortType.OUT.getXMLtag());
			for (Node op : ops)
				ports.add(new OutPort(op));
		}

		Node rootProperties = XMLHelpers.findChild(node, XML_NODE_PROPERTIES_CONTAINER);
		if (rootProperties != null) {
			Node[] props = XMLHelpers.findChildren(rootProperties, Property.XML_NODE_NAME);
			for (Node prop : props)
				properties.add(new Property(prop));
		}

		Node rootProcessor = XMLHelpers.findChild(node, Processor.XML_NODE_NAME);
		if (rootProcessor != null) {
			processor = new Processor(rootProcessor);
		}

		Node rootScheduler = XMLHelpers.findChild(node, Scheduler.XML_NODE_NAME);
		if (rootScheduler != null) {
			scheduler = new Scheduler(rootScheduler);
		}

		Node rootDispatcher = XMLHelpers.findChild(node, Dispatcher.XML_NODE_NAME);
		if (rootDispatcher != null) {
			dispatcher = new Dispatcher(rootDispatcher);
		}
	}

	public String getId() {
		return id;
	}

	public String getNamespace() {
		return namespace;
	}

	public Processor getProcessor() {
		return processor;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public Changeset[] merge(MediatorSpec newInstance) {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		// ports
		for (Iterator<Port> itr = ports.iterator(); itr.hasNext();) {
			Port old = itr.next();
			String name = old.getName();
			String type = old.getType();

			Port updated = PullElementUtil.pullPort(newInstance, name, type);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			}
		}
		for (Port port : newInstance.getPorts()) {
			ports.add(port);
			retval.add(new Changeset(Operation.ADD, port));
		}

		// properties
		for (Iterator<Property> itr = properties.iterator(); itr.hasNext();) {
			Property old = itr.next();
			String key = old.getKey();

			Property updated = PullElementUtil.pullProperty(newInstance, key);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}
		for (Property property : newInstance.getProperties()) {
			properties.add(property);
			retval.add(new Changeset(Operation.ADD, property));
		}

		// processor
		if (processor == null && newInstance.getProcessor() == null) {
			// nothing to do
		} else if (processor != null && newInstance.getProcessor() == null) {
			retval.add(new Changeset(Operation.REMOVE, processor));
			processor = null;
		} else if (processor == null && newInstance.getProcessor() != null) {
			processor = newInstance.getProcessor();
			retval.add(new Changeset(Operation.ADD, processor));
		} else {
			for (Changeset c : processor.merge(newInstance.getProcessor()))
				retval.add(c);
		}

		// scheduler
		if (scheduler == null && newInstance.getScheduler() == null) {
			// nothing to do
		} else if (scheduler != null && newInstance.getScheduler() == null) {
			retval.add(new Changeset(Operation.REMOVE, scheduler));
			scheduler = null;
		} else if (scheduler == null && newInstance.getScheduler() != null) {
			scheduler = newInstance.getScheduler();
			retval.add(new Changeset(Operation.ADD, scheduler));
		} else {
			for (Changeset c : scheduler.merge(newInstance.getScheduler()))
				retval.add(c);
		}

		// dispatcher
		if (dispatcher == null && newInstance.getDispatcher() == null) {
			// nothing to do
		} else if (dispatcher != null && newInstance.getDispatcher() == null) {
			retval.add(new Changeset(Operation.REMOVE, dispatcher));
			dispatcher = null;
		} else if (dispatcher == null && newInstance.getDispatcher() != null) {
			dispatcher = newInstance.getDispatcher();
			retval.add(new Changeset(Operation.ADD, dispatcher));
		} else {
			for (Changeset c : dispatcher.merge(newInstance.getDispatcher()))
				retval.add(c);
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);

	}

	@Override
	public String toString() {
		return id;
	}

	public static Element createXMLSpec(Document document, Node parent, String id, String namespace) {
		Element child = document.createElement(MediatorSpec.XML_NODE_NAME);
		child.setAttribute(MediatorSpec.XML_ATTR_ID, id);
		child.setAttribute(MediatorSpec.XML_ATTR_NAMESPACE, namespace);
		parent.appendChild(child);

		return child;
	}

	public static Node createXMLPort(Document document, Element spec, String portName, PortType portType) {
		Node ports = XMLHelpers.getOrCreateNode(document, spec, XML_NODE_PORTS_CONTAINER);
		return Port.createXMLPort(document, ports, portName, portType);
	}

	public static Node createMediatorProperty(Document document, Element spec, String key, String value) {
		Node properties = XMLHelpers.getOrCreateNode(document, spec, XML_NODE_PROPERTIES_CONTAINER);
		return Property.createXMLProperty(document, properties, key, value);
	}

	public static Node createSchedulerParameter(Document document, Element spec, String param) {
		return Scheduler.createXMLParameter(document, spec, param);
	}

	public static Node createProcessorParameter(Document document, Element spec, String param) {
		return Processor.createXMLParameter(document, spec, param);
	}

	public static Node createDispatcherParameter(Document document, Element spec, String param) {
		return Dispatcher.createXMLParameter(document, spec, param);
	}
}
