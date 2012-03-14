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
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

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
}
