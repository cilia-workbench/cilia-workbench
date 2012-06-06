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
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * A component is a "node" in an abstract Cilia chain. It can be a terminal node
 * (Adapter) or an in-chain node (mediator). The component can be an
 * implementation or a specification.
 * 
 * @author Etienne Gandrille
 */
public abstract class Component implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	/** The component id, unique in the chain */
	private String id;
	/** The id of the specification or implementation pointed by the component */
	protected String type;
	/**
	 * The namespace of the specification or implementation pointed by the
	 * component
	 */
	private String namespace;

	private Chain chain;

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_TYPE = "type";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public Component(Node node, Chain parent) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "id");
		ReflectionUtil.setAttribute(node, XML_ATTR_TYPE, this, "type");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");
		this.chain = parent;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	public Chain getChain() {
		return chain;
	}

	public NameNamespaceID getReferencedTypeID() {
		return new NameNamespaceID(type, namespace);
	}

	public abstract Object getReferencedObject();

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
