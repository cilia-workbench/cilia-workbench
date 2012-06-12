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
package fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericParameter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IDispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IProcessor;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IScheduler;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class MediatorRef extends ComponentRef {

	private List<Parameter> schedulerParameters = new ArrayList<Parameter>();
	private List<Parameter> processorParameters = new ArrayList<Parameter>();
	private List<Parameter> dispatcherParameters = new ArrayList<Parameter>();

	public MediatorRef(Node node, NameNamespaceID chainId) throws CiliaException {
		super(node, chainId);

		initSPD(node, "scheduler", schedulerParameters);
		initSPD(node, "processor", processorParameters);
		initSPD(node, "dispatcher", dispatcherParameters);
	}

	public List<Parameter> getSchedulerParameters() {
		return schedulerParameters;
	}

	public List<Parameter> getProcessorParameters() {
		return processorParameters;
	}

	public List<Parameter> getDispatcherParameters() {
		return dispatcherParameters;
	}

	private static void initSPD(Node node, String subNode, List<Parameter> list) {
		Node root = XMLHelpers.findChild(node, subNode);
		if (root != null) {
			Node[] sub = XMLHelpers.findChildren(root, Parameter.XML_ROOT_NAME);
			for (Node n : sub) {
				try {
					list.add(new Parameter(n));
				} catch (CiliaException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract IGenericMediator getReferencedObject();

	private List<? extends GenericParameter> getReferencedObjectSchedulerParameters() {

		IGenericMediator ro = getReferencedObject();
		if (ro == null)
			return null;

		IScheduler part = ro.getScheduler();
		if (part == null)
			return null;

		return part.getParameters();
	}

	private List<? extends GenericParameter> getReferencedObjectProcessorParameters() {

		IGenericMediator ro = getReferencedObject();
		if (ro == null)
			return null;

		IProcessor part = ro.getProcessor();
		if (part == null)
			return null;

		return part.getParameters();
	}

	private List<? extends GenericParameter> getReferencedObjectDispatcherParameters() {

		IGenericMediator ro = getReferencedObject();
		if (ro == null)
			return null;

		IDispatcher part = ro.getDispatcher();
		if (part == null)
			return null;

		return part.getParameters();
	}

	public List<? extends IGenericPort> getPorts() {
		if (getReferencedObject() != null)
			return getReferencedObject().getPorts();
		return new ArrayList<IGenericPort>();
	}

	public List<? extends IGenericPort> getInPorts() {
		if (getReferencedObject() != null)
			return getReferencedObject().getInPorts();
		return new ArrayList<IGenericPort>();
	}

	public List<? extends IGenericPort> getOutPorts() {
		if (getReferencedObject() != null)
			return getReferencedObject().getOutPorts();
		return new ArrayList<IGenericPort>();
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);

		MediatorRef newInstance = (MediatorRef) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getSchedulerParameters(), schedulerParameters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getProcessorParameters(), processorParameters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getDispatcherParameters(), dispatcherParameters));

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, schedulerParameters));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, processorParameters));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, dispatcherParameters));

		list.addAll(checkParameters(schedulerParameters, getReferencedObjectSchedulerParameters(), "scheduler"));
		list.addAll(checkParameters(processorParameters, getReferencedObjectProcessorParameters(), "processor"));
		list.addAll(checkParameters(dispatcherParameters, getReferencedObjectDispatcherParameters(), "dispatcher"));

		for (CiliaFlag f : tab)
			list.add(f);

		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getIncommingBindings().length == 0)
			e1 = new CiliaError(this + " doesn't have an incomming binding", this);

		if (getOutgoingBindings().length == 0)
			e2 = new CiliaError(this + " doesn't have an outgoing binding", this);

		return CiliaFlag.generateTab(list, e1, e2);
	}

	private List<CiliaFlag> checkParameters(List<Parameter> curParameters,
			List<? extends GenericParameter> typeParameters, String displayName) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		if (typeParameters == null)
			return retval;

		for (Parameter p : curParameters) {
			String name = p.getName();

			boolean found = false;
			for (Iterator<? extends GenericParameter> it = typeParameters.iterator(); !found && it.hasNext();) {
				if (it.next().getName().equalsIgnoreCase(name))
					found = true;
			}

			if (!found)
				retval.add(new CiliaError("Parameter " + name + " not defined in mediator " + displayName, this));
		}

		return retval;
	}
}
