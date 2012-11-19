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
import java.util.Map;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentPart;
import fr.liglab.adele.cilia.workbench.common.parser.element.IPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.parser.element.Property;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.PropertySpec;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorImplem extends Mediator {

	private PortsList ports;

	public static final String XML_NODE_NAME = "mediator-component";

	private final RefMediatorSpec spec;

	private String name;
	private String namespace;

	private String schedulerName;
	private String schedulerNamespace;

	private String processorName;
	private String processorNamespace;

	private String dispatcherName;
	private String dispatcherNamespace;

	private List<PropertyImplem> properties = new ArrayList<PropertyImplem>();

	public MediatorImplem(Node node) throws CiliaException {

		ReflectionUtil.setAttribute(node, "name", this, "name");
		ReflectionUtil.setAttribute(node, "namespace", this, "namespace", CiliaConstants.CILIA_DEFAULT_NAMESPACE);

		String defNs = CiliaConstants.CILIA_DEFAULT_NAMESPACE;
		String specName = null;
		String specNamespace = null;

		Map<String, String> attrMap = XMLHelpers.findAttributesValues(node);
		for (String attr : attrMap.keySet()) {
			if (attr.equalsIgnoreCase("spec-name"))
				specName = attrMap.get(attr);
			else if (attr.equalsIgnoreCase("spec-namespace"))
				specNamespace = attrMap.get(attr);
			else if (!attr.equalsIgnoreCase("name") && !attr.equalsIgnoreCase("namespace"))
				properties.add(new PropertyImplem(attr, attrMap.get(attr)));
		}

		if (specName != null)
			spec = new RefMediatorSpec(specName, specNamespace);
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

		ports = new PortsList(node);
	}

	@Override
	public List<? extends IPort> getPorts() {
		return ports.getPorts();
	}

	@Override
	public NameNamespaceID getId() {
		return new NameNamespaceID(name, namespace);
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	/**
	 * The qualified name is composed by the namespace and the name. If the
	 * namespace is unavailable, this function returns the name.
	 * 
	 * @return the qualified name
	 */
	public String getQualifiedName() {
		return new NameNamespaceID(name, namespace).getQualifiedName();
	}

	@Override
	public String toString() {
		return name;
	}

	public RefMediatorSpec getSpec() {
		return spec;
	}

	public List<PropertyImplem> getProperties() {
		return properties;
	}

	public NameNamespaceID getSchedulerID() {
		return new NameNamespaceID(schedulerName, schedulerNamespace);
	}

	public SchedulerImplem getScheduler() {
		NameNamespaceID id = getSchedulerID();
		return JarRepoService.getInstance().getScheduler(id);
	}

	public NameNamespaceID getProcessorID() {
		return new NameNamespaceID(processorName, processorNamespace);
	}

	public ProcessorImplem getProcessor() {
		NameNamespaceID id = getProcessorID();
		return JarRepoService.getInstance().getProcessor(id);
	}

	public NameNamespaceID getDispatcherID() {
		return new NameNamespaceID(dispatcherName, dispatcherNamespace);
	}

	public DispatcherImplem getDispatcher() {
		NameNamespaceID id = getDispatcherID();
		return JarRepoService.getInstance().getDispatcher(id);
	}

	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = new CiliaFlag[0]; // super.getErrorsAndWarnings();

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
		CiliaFlag e8 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e9 = CiliaWarning.checkStringNotNullOrEmpty(this, namespace, "namespace");

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
			for (PropertySpec mediaProp : mediatorSpec.getProperties()) {
				String specKey = mediaProp.getName();
				Property curProp = getProperty(specKey);
				if (curProp == null)
					flagsTab.add(new CiliaError("Mediator must have \"" + specKey + "\" property defined to respect its specification", getSpec()));
			}

			// Parameters
			flagsTab.addAll(checkMediatorParameters(this));
		}

		return CiliaFlag.generateTab(flagsTab, e1, e2, e3, e4, e5, e6, e7, e8, e9);
	}

	public static List<CiliaFlag> checkMediatorParameters(MediatorImplem mediator) {
		List<CiliaFlag> flagsTab = new ArrayList<CiliaFlag>();

		MediatorSpec mediatorSpec = mediator.getSpec().getMediatorSpec();
		String mediatorSpecName = mediator.getSpec().toString();

		// Scheduler parameters
		SchedulerImplem scheduler = mediator.getScheduler();
		flagsTab.addAll(checkParameters(mediatorSpecName, mediatorSpec.getScheduler(), scheduler, "scheduler"));

		// Processor parameters
		ProcessorImplem processor = mediator.getProcessor();
		flagsTab.addAll(checkParameters(mediatorSpecName, mediatorSpec.getProcessor(), processor, "processor"));

		// Dispatcher parameters
		DispatcherImplem dispatcher = mediator.getDispatcher();
		flagsTab.addAll(checkParameters(mediatorSpecName, mediatorSpec.getDispatcher(), dispatcher, "dispatcher"));

		return flagsTab;
	}

	private static List<CiliaFlag> checkParameters(String specName, ComponentPart spec, ComponentPart implem, String elementTypeName) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		if (spec != null) {
			for (ParameterDefinition param : spec.getParameters()) {
				String paramName = param.getName();

				if (implem == null || implem.getParameter(paramName) == null)
					retval.add(new CiliaError(elementTypeName + " must have a parameter named " + paramName + " to respect its specification", specName));
			}
		}

		if (implem != null) {
			for (ParameterDefinition param : implem.getParameters()) {
				String paramName = param.getName();

				if (spec == null || spec.getParameter(paramName) == null)
					retval.add(new CiliaError(elementTypeName + " has an extra parameter named " + paramName + " not defined in its specification", specName));
			}
		}

		return retval;
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}

	/**
	 * An object, which can give a pointer (which can be null...) to a
	 * Specification. Used for display purpose using a label provider.
	 */
	public class RefMediatorSpec extends NameNamespace implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

		public RefMediatorSpec(String name, String namespace) {
			super(name, namespace);
		}

		public MediatorSpec getMediatorSpec() {
			return SpecRepoService.getInstance().getMediatorSpec(getId());
		}

		@Override
		public CiliaFlag[] getErrorsAndWarnings() {
			CiliaFlag[] tab = super.getErrorsAndWarnings();

			CiliaError e = null;

			if (getMediatorSpec() == null)
				e = new CiliaError("Can't find mediator spec " + getQualifiedName(), this);

			return CiliaFlag.generateTab(tab, e);
		}
	}
}