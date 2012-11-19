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
import fr.liglab.adele.cilia.workbench.common.parser.element.Component;
import fr.liglab.adele.cilia.workbench.common.parser.element.IPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.InPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutPort;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * A componentRef is a "node" in an abstract Cilia chain. It can be a terminal
 * node (Adapter) or an in-chain node (mediator). The component can be an
 * implementation or a specification.
 * 
 * There is an IMPORTANT distinction between Component and ComponentRef
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
	protected String id;

	/** The referenced component ID */
	private final NameNamespaceID referencedComponentID;

	public ComponentRef(String id, String type, String namespace) {
		this.id = id;
		this.referencedComponentID = new NameNamespaceID(type, namespace);
	}

	// CHAIN RELATIVE
	// ==============

	public abstract IChain getChain();

	public abstract Component getReferencedComponent();

	public NameNamespaceID getReferencedTypeID() {
		return referencedComponentID;
	}

	// MISC
	// ====

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		ComponentRef newRef = (ComponentRef) other;

		retval.addAll(MergeUtil.computeUpdateChangeset(newRef.getReferencedTypeID(), referencedComponentID, "name"));
		retval.addAll(MergeUtil.computeUpdateChangeset(newRef.getReferencedTypeID(), referencedComponentID, "namespace"));

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id, "id");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, referencedComponentID.getName(), "type");
		CiliaFlag e3 = CiliaWarning.checkStringNotNullOrEmpty(this, referencedComponentID.getNamespace(), "namespace");

		return CiliaFlag.generateTab(e1, e2, e3);
	}

	// BINDINGS
	// ========

	public Binding[] getBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getChain().getBindings()) {
			if (b.getDestinationId().equals(id))
				retval.add(b);
			else if (b.getSourceId().equals(id))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding[] getIncommingBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getBindings()) {
			if (b.getDestinationId().equals(id))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding[] getOutgoingBindings() {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding b : getBindings()) {
			if (b.getSourceId().equals(id))
				retval.add(b);
		}

		return retval.toArray(new Binding[0]);
	}

	public Binding getIncommingBinding(ComponentRef source) {
		for (Binding b : getIncommingBindings())
			if (b.getSourceComponent() != null && b.getSourceComponent().equals(source))
				return b;
		return null;
	}

	public Binding getOutgoingBinding(ComponentRef destination) {
		for (Binding b : getOutgoingBindings())
			if (b.getDestinationComponent() != null && b.getDestinationComponent().equals(destination))
				return b;
		return null;
	}

	// PORTS
	// =====

	public List<? extends IPort> getPorts() {
		if (getReferencedComponent() != null)
			return getReferencedComponent().getPorts();
		return new ArrayList<IPort>();
	}

	public List<InPort> getInPorts() {
		if (getReferencedComponent() != null)
			return getReferencedComponent().getInPorts();
		return new ArrayList<InPort>();
	}

	public List<OutPort> getOutPorts() {
		if (getReferencedComponent() != null)
			return getReferencedComponent().getOutPorts();
		return new ArrayList<OutPort>();
	}
}