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
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphDrawable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Chain implements DisplayedInPropertiesView, Identifiable, ErrorsAndWarningsFinder, Mergeable, GraphDrawable {

	private final String name;

	protected List<AdapterRef> adapters = new ArrayList<AdapterRef>();
	protected List<MediatorRef> mediators = new ArrayList<MediatorRef>();
	protected List<Binding> bindings = new ArrayList<Binding>();

	public Chain(String name) {
		this.name = name;
	}

	public ComponentRef getComponent(String componentId) {
		for (AdapterRef adapter : getAdapters())
			if (adapter.getId().equals(componentId))
				return adapter;
		for (MediatorRef mediator : getMediators())
			if (mediator.getId().equals(componentId))
				return mediator;
		return null;
	}

	/**
	 * Finds the {@link ComponentDefinition} referenced by the chain component
	 * with id given into parameter. If the component can't be located, throws
	 * an exception containing an error message.
	 * 
	 * @param componentID
	 * @return
	 * @throws CiliaException
	 */
	public ComponentDefinition getReferencedComponent(String componentID) throws CiliaException {

		if (Strings.isNullOrEmpty(componentID))
			throw new CiliaException("id is null or empty");

		ComponentRef component = getComponent(componentID);
		if (component == null)
			throw new CiliaException("can't find component with id " + componentID);

		return component.getReferencedComponentDefinition();
	}

	public List<AdapterRef> getAdapters() {
		return adapters;
	}

	public List<MediatorRef> getMediators() {
		return mediators;
	}

	public List<Binding> getBindings() {
		return bindings;
	}

	public ComponentRef[] getComponents() {
		List<ComponentRef> retval = new ArrayList<ComponentRef>();
		for (AdapterRef adapter : getAdapters())
			retval.add(adapter);
		for (MediatorRef mediator : getMediators())
			retval.add(mediator);
		return retval.toArray(new ComponentRef[0]);
	}

	public List<Binding> getIncomingBindings(String element) {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding binding : getBindings()) {
			if (binding.getDestinationId().equals(element))
				retval.add(binding);
		}

		return retval;
	}

	public List<Binding> getOutgoingBindings(String element) {
		List<Binding> retval = new ArrayList<Binding>();

		for (Binding binding : getBindings()) {
			if (binding.getSourceId().equals(element))
				retval.add(binding);
		}

		return retval;
	}

	@Override
	public Object[] getElements() {
		return getComponents();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return Strings.nullToEmpty(name);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();
		Chain newInstance = (Chain) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getAdapters(), adapters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getMediators(), mediators));
		retval.addAll(MergeUtil.mergeLists(newInstance.getBindings(), bindings));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> list = IdentifiableUtils.getErrorsNonUniqueId(this, getComponents());
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");

		return CiliaFlag.generateTab(list, e1);
	}
}