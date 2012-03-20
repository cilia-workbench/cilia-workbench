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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpec {

	private String id;
	private String namespace;

	private List<Port> ports = new ArrayList<Port>();
	private List<Property> properties = new ArrayList<Property>();
	private Processor processor = null;
	private Scheduler scheduler = null;
	private Dispatcher dispatcher = null;
	private Node node;

	public MediatorSpec(Node node) throws MetadataException {
		this.node = node;
		XMLReflectionUtil.setRequiredAttribute(node, "id", this, "id");
		XMLReflectionUtil.setOptionalAttribute(node, "namespace", this, "namespace");

		Node rootPorts = XMLReflectionUtil.findChild(node, "ports");
		if (rootPorts != null) {
			Node[] ips = XMLReflectionUtil.findChildren(rootPorts, "in-port");
			for (Node ip : ips)
				ports.add(new InPort(ip));
			Node[] ops = XMLReflectionUtil.findChildren(rootPorts, "out-port");
			for (Node op : ops)
				ports.add(new OutPort(op));
		}

		Node rootProperties = XMLReflectionUtil.findChild(node, "properties");
		if (rootProperties != null) {
			Node[] props = XMLReflectionUtil.findChildren(rootProperties, "property");
			for (Node prop : props)
				properties.add(new Property(prop));
		}

		Node rootProcessor = XMLReflectionUtil.findChild(node, "processor");
		if (rootProcessor != null) {
			processor = new Processor(rootProcessor);
		}

		Node rootScheduler = XMLReflectionUtil.findChild(node, "scheduler");
		if (rootScheduler != null) {
			scheduler = new Scheduler(rootScheduler);
		}

		Node rootDispatcher = XMLReflectionUtil.findChild(node, "dispatcher");
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
			String value = old.getValue();

			Property updated = PullElementUtil.pullProperty(newInstance, key, value);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
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
}
