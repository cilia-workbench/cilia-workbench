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
package fr.liglab.adele.cilia.workbench.common.parser.chain;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.common.parser.element.InPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.Port;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * A componentRef is a "node" in an abstract Cilia chain. It can be a terminal
 * node (Adapter) or an in-chain node (mediator). The component can be an
 * implementation or a specification.
 * 
 * There is an IMPORTANT distinction between ComponentDefinition and
 * ComponentRef
 * <ul>
 * <li>A component is a specification or an implementation.</li>
 * <li>A component ref reference a specification or an implementation. It's NOT
 * the specification or the implementation itself</li>
 * </ul>
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentRef implements Identifiable, ErrorsAndWarningsFinder, Mergeable, DisplayedInPropertiesView {

	/** The component id, unique in the chain */
	protected String componentID;

	/** The referenced component ID */
	private final NameNamespaceID referencedComponentID;

	/** Repository used to find the component from this componentRef */
	protected final ComponentRepoService<?, ?> componentRepo;

	// CONSTRUCTOR
	// ===========

	public ComponentRef(String componentID, NameNamespaceID referencedComponentID) {
		this.componentID = componentID;
		if (referencedComponentID != null)
			this.referencedComponentID = referencedComponentID;
		else
			this.referencedComponentID = new NameNamespaceID();
		this.componentRepo = getComponentRepoService();
	}

	// RELATIVE CHAIN
	// ==============

	public abstract Chain getChain();

	protected abstract ComponentRepoService<?, ?> getComponentRepoService();

	// REFERENCED COMPONENT
	// ====================

	public NameNamespaceID getReferencedComponentDefinitionID() {
		return referencedComponentID;
	}

	public abstract ComponentDefinition getReferencedComponentDefinition();

	// BINDINGS
	// ========

	public Binding[] getBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getChain().getBindings()) {
			if (b.getDestinationId().equals(componentID))
				retval.add(b);
			else if (b.getSourceId().equals(componentID))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding[] getIncommingBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getBindings()) {
			if (b.getDestinationId().equals(componentID))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding[] getOutgoingBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getBindings()) {
			if (b.getSourceId().equals(componentID))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding getIncommingBinding(ComponentRef source) {
		for (Binding b : getIncommingBindings())
			if (b.getSourceComponentRef() != null && b.getSourceComponentRef().equals(source))
				return b;
		return null;
	}

	public Binding getOutgoingBinding(ComponentRef destination) {
		for (Binding b : getOutgoingBindings())
			if (b.getDestinationComponentRef() != null && b.getDestinationComponentRef().equals(destination))
				return b;
		return null;
	}

	// PORTS
	// =====

	public List<? extends Port> getPorts() {
		if (getReferencedComponentDefinition() != null)
			return getReferencedComponentDefinition().getPorts();
		return new ArrayList<Port>();
	}

	public List<InPort> getInPorts() {
		if (getReferencedComponentDefinition() != null)
			return getReferencedComponentDefinition().getInPorts();
		return new ArrayList<InPort>();
	}

	public List<OutPort> getOutPorts() {
		if (getReferencedComponentDefinition() != null)
			return getReferencedComponentDefinition().getOutPorts();
		return new ArrayList<OutPort>();
	}

	// MISC
	// ====

	@Override
	public String getId() {
		return componentID;
	}

	@Override
	public String toString() {
		return Strings.nullToEmpty(componentID);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		ComponentRef newRef = (ComponentRef) other;

		retval.addAll(MergeUtil.computeUpdateChangeset(newRef.getReferencedComponentDefinitionID(), referencedComponentID, "name"));
		retval.addAll(MergeUtil.computeUpdateChangeset(newRef.getReferencedComponentDefinitionID(), referencedComponentID, "namespace"));

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, componentID, "id");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, referencedComponentID.getName(), "type");
		CiliaFlag e3 = CiliaWarning.checkStringNotNullOrEmpty(this, referencedComponentID.getNamespace(), "namespace");
		CiliaFlag e4 = null;

		if (getReferencedComponentDefinition() == null)
			e4 = new CiliaError("Can't find mediator definition for " + this, this);

		return CiliaFlag.generateTab(e1, e2, e3, e4);
	}
}