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
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericInPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericOutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameValueProperty;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericPort.PortNature;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.ComponentPart;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.NameProperty;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorComponent extends NameNamespace implements IGenericMediator, DisplayedInPropertiesView {

	public static final String XML_NODE_NAME = "mediator-component";

	private final SuperMediator spec;

	private String schedulerName;
	private String schedulerNamespace;

	private String processorName;
	private String processorNamespace;

	private String dispatcherName;
	private String dispatcherNamespace;

	private List<IGenericPort> ports = new ArrayList<IGenericPort>();

	private List<NameValueProperty> properties = new ArrayList<NameValueProperty>();

	public MediatorComponent(Node node) throws CiliaException {

		ReflectionUtil.setAttribute(node, "name", this, "name");
		ReflectionUtil.setAttribute(node, "namespace", this, "namespace", CiliaConstants.getDefaultNamespace());

		String defNs = CiliaConstants.getDefaultNamespace();
		String specName = null;
		String specNamespace = null;

		Map<String, String> attrMap = XMLHelpers.findAttributesValues(node);
		for (String attr : attrMap.keySet()) {
			if (attr.equalsIgnoreCase("spec-name"))
				specName = attrMap.get(attr);
			else if (attr.equalsIgnoreCase("spec-namespace"))
				specNamespace = attrMap.get(attr);
			else if (!attr.equalsIgnoreCase("name") && !attr.equalsIgnoreCase("namespace"))
				properties.add(new NameValueProperty(attr, attrMap.get(attr)));
		}

		if (specName != null)
			spec = new SuperMediator(specName, specNamespace);
		else
			spec = null;

		Node schedulerNode = XMLHelpers.findChild(node, "scheduler");
		if (schedulerNode != null) {
			ReflectionUtil.setAttribute(schedulerNode, "name", this, "schedulerName");
			ReflectionUtil.setAttribute(schedulerNode, "namespace", this, "schedulerNamespace", defNs);
		}

		Node processorNode = XMLHelpers.findChild(node, "processor");
		if (processorNode != null) {
			ReflectionUtil.setAttribute(processorNode, "name", this, "processorName");
			ReflectionUtil.setAttribute(processorNode, "namespace", this, "processorNamespace", defNs);
		}

		Node dispatcherNode = XMLHelpers.findChild(node, "dispatcher");
		if (dispatcherNode != null) {
			ReflectionUtil.setAttribute(dispatcherNode, "name", this, "dispatcherName");
			ReflectionUtil.setAttribute(dispatcherNode, "namespace", this, "dispatcherNamespace", defNs);
		}

		Node portsNode = XMLHelpers.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLHelpers.findChildren(portsNode, InPort.XML_NODE_NAME);
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLHelpers.findChildren(portsNode, OutPort.XML_NODE_NAME);
			for (Node outPort : outPorts)
				ports.add(new OutPort(outPort));
		}
	}

	public List<IGenericPort> getPorts() {
		return ports;
	}

	public List<GenericInPort> getInPorts() {
		List<GenericInPort> retval = new ArrayList<GenericInPort>();
		for (IGenericPort p : ports)
			if (p.getNature() == PortNature.IN)
				retval.add((GenericInPort) p);
		return retval;
	}

	public List<GenericOutPort> getOutPorts() {
		List<GenericOutPort> retval = new ArrayList<GenericOutPort>();
		for (IGenericPort p : ports)
			if (p.getNature() == PortNature.OUT)
				retval.add((GenericOutPort) p);
		return retval;
	}

	public SuperMediator getSpec() {
		return spec;
	}

	public List<NameValueProperty> getProperties() {
		return properties;
	}

	public NameValueProperty getProperty(String key) {
		for (NameValueProperty p : getProperties())
			if (p.getName().equalsIgnoreCase(key))
				return p;
		return null;
	}

	public NameNamespaceID getSchedulerID() {
		return new NameNamespaceID(schedulerName, schedulerNamespace);
	}

	public Scheduler getScheduler() {
		NameNamespaceID id = getSchedulerID();
		return JarRepoService.getInstance().getScheduler(id);
	}

	public NameNamespaceID getProcessorID() {
		return new NameNamespaceID(processorName, processorNamespace);
	}

	public Processor getProcessor() {
		NameNamespaceID id = getProcessorID();
		return JarRepoService.getInstance().getProcessor(id);
	}

	public NameNamespaceID getDispatcherID() {
		return new NameNamespaceID(dispatcherName, dispatcherNamespace);
	}

	public Dispatcher getDispatcher() {
		NameNamespaceID id = getDispatcherID();
		return JarRepoService.getInstance().getDispatcher(id);
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
		CiliaFlag e6 = null;
		CiliaFlag e7 = null;

		// ports
		if (getInPorts().size() == 0)
			e4 = new CiliaError("Mediator doesn't have an in port", this);
		if (getOutPorts().size() == 0)
			e5 = new CiliaError("Mediator doesn't have an out port", this);
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getInPorts()));
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getOutPorts()));

		// properties
		flagsTab.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, properties));

		// Spec
		if (getSpec() != null && getSpec().getMediatorSpec() != null) {
			MediatorSpec mediatorSpec = getSpec().getMediatorSpec();

			// In ports
			if (!IdentifiableUtils.isSameListId(mediatorSpec.getInPorts(), getInPorts()))
				e6 = new CiliaError("In ports list doesn't respect the specification", getSpec());

			// Out ports
			if (!IdentifiableUtils.isSameListId(mediatorSpec.getOutPorts(), getOutPorts()))
				e7 = new CiliaError("Out ports list doesn't respect the specification", getSpec());

			// Spec properties must exists in instance
			for (NameProperty mediaProp : mediatorSpec.getProperties()) {
				String specKey = mediaProp.getName();
				NameValueProperty curProp = getProperty(specKey);
				if (curProp == null)
					flagsTab.add(new CiliaError("Mediator must have \"" + specKey
							+ "\" property defined to respect its specification", getSpec()));
			}

			// Parameters
			flagsTab.addAll(checkMediatorParameters(this));
		}

		return CiliaFlag.generateTab(flagsTab, e1, e2, e3, e4, e5, e6, e7);
	}

	public static List<CiliaFlag> checkMediatorParameters(MediatorComponent mediator) {
		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();

		MediatorSpec mediatorSpec = mediator.getSpec().getMediatorSpec();

		// Scheduler parameters
		Scheduler scheduler = mediator.getScheduler();
		flagsTab.addAll(checkParameters(mediator, mediatorSpec.getScheduler(), scheduler, "scheduler"));

		// Processor parameters
		Processor processor = mediator.getProcessor();
		flagsTab.addAll(checkParameters(mediator, mediatorSpec.getProcessor(), processor, "processor"));

		// Dispatcher parameters
		Dispatcher dispatcher = mediator.getDispatcher();
		flagsTab.addAll(checkParameters(mediator, mediatorSpec.getDispatcher(), dispatcher, "dispatcher"));

		return flagsTab;
	}

	private static List<CiliaFlag> checkParameters(MediatorComponent mediator, ComponentPart spec, SPDElement implem,
			String elementTypeName) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		if (spec != null) {
			for (fr.liglab.adele.cilia.workbench.designer.parser.spec.Parameter param : spec.getParameters()) {
				String paramName = param.getName();

				if (implem == null || implem.getParameter(paramName) == null)
					retval.add(new CiliaError(elementTypeName + " must have a parameter named " + paramName
							+ " to respect its specification", mediator.getSpec()));
			}
		}

		if (implem != null) {
			for (Parameter param : implem.getParameters()) {
				String paramName = param.getName();

				if (spec == null || spec.getParameter(paramName) == null)
					retval.add(new CiliaError(elementTypeName + " has an extra parameter named " + paramName
							+ " not defined in its specification", mediator.getSpec()));
			}
		}

		return retval;
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}
}
