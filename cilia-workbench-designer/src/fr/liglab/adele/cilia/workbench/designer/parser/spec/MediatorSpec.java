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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpec implements IMediator, DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable,
		Mergeable {

	public static final String XML_NODE_NAME = "mediator-specification";

	public static final String XML_NODE_PROPERTIES_CONTAINER = "properties";
	public static final String XML_NODE_PORTS_CONTAINER = "ports";

	private String id;
	public static final String XML_ATTR_ID = "id";
	private String namespace;
	public static final String XML_ATTR_NAMESPACE = "namespace";

	private List<GenericPort> ports = new ArrayList<GenericPort>();
	private List<Property> properties = new ArrayList<Property>();
	private Processor processor = null;
	private Scheduler scheduler = null;
	private Dispatcher dispatcher = null;

	public MediatorSpec(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "id");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");

		Node rootPorts = XMLHelpers.findChild(node, XML_NODE_PORTS_CONTAINER);
		if (rootPorts != null) {
			Node[] ips = XMLHelpers.findChildren(rootPorts, InPort.getXMLtag());
			for (Node ip : ips)
				ports.add(new InPort(ip));
			Node[] ops = XMLHelpers.findChildren(rootPorts, OutPort.getXMLtag());
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

	public NameNamespaceID getId() {
		return new NameNamespaceID(id, namespace);
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

	public List<GenericPort> getPorts() {
		return ports;
	}

	public List<InPort> getInPorts() {
		List<InPort> retval = new ArrayList<InPort>();
		for (GenericPort p : ports)
			if (p.isInPort())
				retval.add((InPort) p);
		return retval;
	}

	public List<OutPort> getOutPorts() {
		List<OutPort> retval = new ArrayList<OutPort>();
		for (GenericPort p : ports)
			if (p.isOutPort())
				retval.add((OutPort) p);
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.
	 * Mergeable#merge(java.lang.Object)
	 */
	@Override
	public List<Changeset> merge(Object other) throws CiliaException {

		List<Changeset> retval = new ArrayList<Changeset>();
		MediatorSpec newInstance = (MediatorSpec) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getPorts(), ports));
		retval.addAll(MergeUtil.mergeLists(newInstance.getProperties(), properties));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "processor"));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "scheduler"));
		retval.addAll(MergeUtil.mergeObjectsFields(newInstance, this, "dispatcher"));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	public String toString() {
		return id;
	}

	public static Element createXMLSpec(Document document, Node parent, NameNamespaceID id) {
		Element child = document.createElement(MediatorSpec.XML_NODE_NAME);
		child.setAttribute(MediatorSpec.XML_ATTR_ID, id.getName());
		child.setAttribute(MediatorSpec.XML_ATTR_NAMESPACE, id.getNamespace());
		parent.appendChild(child);

		return child;
	}

	public static Node createXMLInPort(Document document, Element spec, String portName) {
		Node ports = XMLHelpers.getOrCreateNode(document, spec, XML_NODE_PORTS_CONTAINER);
		return InPort.createXMLPort(document, ports, portName);
	}

	public static Node createXMLOutPort(Document document, Element spec, String portName) {
		Node ports = XMLHelpers.getOrCreateNode(document, spec, XML_NODE_PORTS_CONTAINER);
		return OutPort.createXMLPort(document, ports, portName);
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

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {

		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id, "id");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, namespace, "namespace");
		CiliaFlag e3 = null;
		CiliaFlag e4 = null;

		// ports
		if (getInPorts().size() == 0)
			e3 = new CiliaError("Mediator doesn't have an in port", this);
		if (getOutPorts().size() == 0)
			e4 = new CiliaError("Mediator doesn't have an out port", this);
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		// properties
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, properties));

		return CiliaFlag.generateTab(flagsTab, e1, e2, e3, e4);
	}
}
