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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ParameterRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.chain.ChainRepoService;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLComponentRefHelper;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLParameterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.PropertySpec;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpecRef extends MediatorRef {

	public static final String XML_NODE_NAME = "mediator-specification";
	public static final String XML_SELECTION_CONSTRAINT = "selection-constraint";

	private List<PropertyConstraint> constraints = new ArrayList<PropertyConstraint>();

	/** the chainID, which hosts the current {@link ComponentRef} */
	NameNamespaceID chainId;

	private final ChainRepoService<?, ?, ?> repo;

	public static String XML_SCHEDULER_NODE = "scheduler";
	public static String XML_PROCESSOR_NODE = "processor";
	public static String XML_DISPATCHER_NODE = "dispatcher";

	private List<ParameterRef> schedulerParameters = new ArrayList<ParameterRef>();
	private List<ParameterRef> processorParameters = new ArrayList<ParameterRef>();
	private List<ParameterRef> dispatcherParameters = new ArrayList<ParameterRef>();

	public MediatorSpecRef(Node node, NameNamespaceID chainId, ChainRepoService<?, ?, ?> repo) throws CiliaException {
		super(XMLComponentRefHelper.getId(node), new NameNamespaceID(XMLComponentRefHelper.getType(node), XMLComponentRefHelper.getNamespace(node)));

		this.chainId = chainId;
		this.repo = repo;

		initSPDparameters(node, XML_SCHEDULER_NODE, schedulerParameters);
		initSPDparameters(node, XML_PROCESSOR_NODE, processorParameters);
		initSPDparameters(node, XML_DISPATCHER_NODE, dispatcherParameters);

		Node rootConstraint = XMLHelpers.findChild(node, XML_SELECTION_CONSTRAINT);
		if (rootConstraint != null) {
			Node[] sub = XMLHelpers.findChildren(rootConstraint, PropertyConstraint.XML_PROPERTY_CONSTRAINT);
			for (Node n : sub) {
				constraints.add(new PropertyConstraint(n));
			}
		}
	}

	public Chain getChain() {
		return repo.findChain(chainId);
	}

	public List<ParameterRef> getSchedulerParameters() {
		return schedulerParameters;
	}

	public List<ParameterRef> getProcessorParameters() {
		return processorParameters;
	}

	public List<ParameterRef> getDispatcherParameters() {
		return dispatcherParameters;
	}

	private static void initSPDparameters(Node node, String subNode, List<ParameterRef> list) {
		Node root = XMLHelpers.findChild(node, subNode);
		if (root != null) {
			Node[] sub = XMLHelpers.findChildren(root, XMLParameterRef.XML_ROOT_NAME);
			for (Node n : sub) {
				try {
					list.add(new XMLParameterRef(n));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);

		MediatorSpecRef newInstance = (MediatorSpecRef) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getSchedulerParameters(), schedulerParameters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getProcessorParameters(), processorParameters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getDispatcherParameters(), dispatcherParameters));

		// constraints
		retval.addAll(MergeUtil.mergeLists(newInstance.getConstraints(), constraints));

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, schedulerParameters));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, processorParameters));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, dispatcherParameters));

		list.addAll(checkParameters(schedulerParameters, getReferencedComponentSchedulerParameters(), "scheduler"));
		list.addAll(checkParameters(processorParameters, getReferencedComponentProcessorParameters(), "processor"));
		list.addAll(checkParameters(dispatcherParameters, getReferencedComponentDispatcherParameters(), "dispatcher"));

		// constraints
		if (getReferencedComponentDefinition() != null) {
			Mediator ro = getReferencedComponentDefinition();

			for (PropertyConstraint pc : constraints)
				if (ro.getProperty(pc.getName()) == null)
					list.add(new CiliaError("Specification " + ((NameNamespaceID) ro.getId()).getName() + " doesn't reference a \"" + pc.getName()
							+ "\" property", this));
		}

		return CiliaFlag.generateTab(tab, list.toArray(new CiliaFlag[0]));
	}

	private List<CiliaFlag> checkParameters(List<ParameterRef> curParameters, List<? extends ParameterDefinition> typeParameters, String displayName) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		if (typeParameters == null)
			return retval;

		for (ParameterRef p : curParameters) {
			String name = p.getName();

			boolean found = false;
			for (Iterator<? extends ParameterDefinition> it = typeParameters.iterator(); !found && it.hasNext();) {
				if (it.next().getName().equalsIgnoreCase(name))
					found = true;
			}

			if (!found)
				retval.add(new CiliaError("Parameter " + name + " not defined in mediator " + displayName, this));
		}

		return retval;
	}

	public List<PropertyConstraint> getConstraints() {
		return constraints;
	}

	public List<PropertySpec> getPossibleConstraints() {

		Mediator ro = getReferencedComponentDefinition();
		if (ro != null && ro instanceof MediatorSpec) {
			MediatorSpec spec = (MediatorSpec) ro;
			return spec.getProperties();
		}

		return new ArrayList<PropertySpec>();
	}

	@Override
	protected ComponentRepoService<?, ?> getComponentRepoService() {
		return SpecRepoService.getInstance();
	}
}
