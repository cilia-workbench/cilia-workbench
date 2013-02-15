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
package fr.liglab.adele.cilia.workbench.designer.parser.element.spec;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.Port;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpec extends Mediator implements Mergeable {

	public static final String XML_NODE_NAME = "mediator-specification";

	public static final String XML_NODE_PROPERTIES_CONTAINER = "properties";
	public static final String XML_NODE_PORTS_CONTAINER = "ports";

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	private List<PropertySpec> properties = new ArrayList<PropertySpec>();
	private ProcessorSpec processor = null;
	private SchedulerSpec scheduler = null;
	private DispatcherSpec dispatcher = null;

	public MediatorSpec(Node node, String physicalResourcePath) throws CiliaException {
		super(computeID(node), computePorts(node), physicalResourcePath);

		Node rootProperties = XMLHelpers.findChild(node, XML_NODE_PROPERTIES_CONTAINER);
		if (rootProperties != null) {
			Node[] props = XMLHelpers.findChildren(rootProperties, PropertySpec.XML_NODE_NAME);
			for (Node prop : props)
				properties.add(new PropertySpec(prop));
		}

		Node rootProcessor = XMLHelpers.findChild(node, ProcessorSpec.XML_NODE_NAME);
		if (rootProcessor != null) {
			processor = new ProcessorSpec(rootProcessor);
		}

		Node rootScheduler = XMLHelpers.findChild(node, SchedulerSpec.XML_NODE_NAME);
		if (rootScheduler != null) {
			scheduler = new SchedulerSpec(rootScheduler);
		}

		Node rootDispatcher = XMLHelpers.findChild(node, DispatcherSpec.XML_NODE_NAME);
		if (rootDispatcher != null) {
			dispatcher = new DispatcherSpec(rootDispatcher);
		}
	}

	private static NameNamespaceID computeID(Node node) {
		String name = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_NAME);
		String namespace = XMLHelpers.findAttributeValue(node, XML_ATTR_NAMESPACE, CiliaConstants.CILIA_DEFAULT_NAMESPACE);
		return new NameNamespaceID(name, namespace);
	}

	private static List<Port> computePorts(Node node) {
		List<Port> ports = new ArrayList<Port>();

		Node rootPorts = XMLHelpers.findChild(node, XML_NODE_PORTS_CONTAINER);
		if (rootPorts != null) {
			Node[] ips = XMLHelpers.findChildren(rootPorts, InPortSpec.getXMLtag());
			for (Node ip : ips) {
				try {
					ports.add(new InPortSpec(ip));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
			Node[] ops = XMLHelpers.findChildren(rootPorts, OutPortSpec.getXMLtag());
			for (Node op : ops) {
				try {
					ports.add(new OutPortSpec(op));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}

		return ports;
	}

	public ProcessorSpec getProcessor() {
		return processor;
	}

	public SchedulerSpec getScheduler() {
		return scheduler;
	}

	public DispatcherSpec getDispatcher() {
		return dispatcher;
	}

	public List<PropertySpec> getProperties() {
		return properties;
	}

	public PropertySpec getProperty(String key) {
		for (PropertySpec p : getProperties())
			if (p.getName().equalsIgnoreCase(key))
				return p;
		return null;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {

		List<Changeset> retval = new ArrayList<Changeset>();
		MediatorSpec newInstance = (MediatorSpec) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getPorts(), getPorts()));
		retval.addAll(MergeUtil.mergeLists(newInstance.getProperties(), properties));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "processor"));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "scheduler"));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "dispatcher"));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public static Element createXMLSpec(Document document, Node parent, NameNamespaceID id) {
		Element child = document.createElement(MediatorSpec.XML_NODE_NAME);
		child.setAttribute(MediatorSpec.XML_ATTR_NAME, id.getName());
		child.setAttribute(MediatorSpec.XML_ATTR_NAMESPACE, id.getNamespace());
		parent.appendChild(child);

		return child;
	}

	public static Node createXMLInPort(Document document, Element spec, String portName, String portType) {
		Node ports = XMLHelpers.getOrCreateChild(document, spec, XML_NODE_PORTS_CONTAINER);
		return InPortSpec.createXMLPort(document, ports, portName, portType);
	}

	public static Node createXMLOutPort(Document document, Element spec, String portName, String portType) {
		Node ports = XMLHelpers.getOrCreateChild(document, spec, XML_NODE_PORTS_CONTAINER);
		return OutPortSpec.createXMLPort(document, ports, portName, portType);
	}

	public static Node createMediatorProperty(Document document, Element spec, String key) {
		Node root = XMLHelpers.getOrCreateChild(document, spec, XML_NODE_PROPERTIES_CONTAINER);
		return XMLHelpers.createChild(document, root, PropertySpec.XML_NODE_NAME, "name", key);
	}

	public static Node createSchedulerParameter(Document document, Element spec, String param) {
		return SchedulerSpec.createXMLParameter(document, spec, param);
	}

	public static Node createProcessorParameter(Document document, Element spec, String param) {
		return ProcessorSpec.createXMLParameter(document, spec, param);
	}

	public static Node createDispatcherParameter(Document document, Element spec, String param) {
		return DispatcherSpec.createXMLParameter(document, spec, param);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.SPEC;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();
		for (CiliaFlag f : tab)
			flagsTab.add(f);

		CiliaFlag e1 = null;
		CiliaFlag e2 = null;

		// ports
		if (getInPorts().size() == 0)
			e1 = new CiliaError("Mediator doesn't have an in port", this);
		if (getOutPorts().size() == 0)
			e2 = new CiliaError("Mediator doesn't have an out port", this);

		// properties
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, properties));

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}
