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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IComponent;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * A component is a "node" in an abstract Cilia chain. It can be a terminal node
 * (Adapter) or an in-chain node (mediator). The component can be an
 * implementation or a specification.
 * 
 * @author Etienne Gandrille
 */
public abstract class ComponentRef implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	/** The component id, unique in the chain */
	private String id;

	/** the chainID, which hosts the current {@link ComponentRef} */
	NameNamespaceID chainId;

	private final ChainRepoService<?, ?, ?> repo;

	// "real" component, pointed by this component
	protected String type;
	private String namespace;

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_TYPE = "type";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public ComponentRef(Node node, NameNamespaceID chainId, ChainRepoService<?, ?, ?> repo) throws CiliaException {
		this.chainId = chainId;
		this.repo = repo;
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "id");
		ReflectionUtil.setAttribute(node, XML_ATTR_TYPE, this, "type");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	private Chain getChain() {
		return repo.findChain(chainId);
	}

	public NameNamespaceID getReferencedTypeID() {
		return new NameNamespaceID(type, namespace);
	}

	public abstract IComponent getReferencedObject();

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

	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id, "id");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, type, "type");
		CiliaFlag e3 = CiliaWarning.checkStringNotNullOrEmpty(this, namespace, "namespace");

		return CiliaFlag.generateTab(e1, e2, e3);
	}

	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();

		retval.addAll(MergeUtil.computeUpdateChangeset(other, this, "type"));
		retval.addAll(MergeUtil.computeUpdateChangeset(other, this, "namespace"));

		return retval;
	}
}
