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

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_NODE_NAME = "binding";

	private Chain chain;
	private String from;
	private String to;
	private Cardinality fromCardinality;
	private Cardinality toCardinality;

	public Binding(Node node, Chain chain) throws CiliaException {
		this.chain = chain;
		ReflectionUtil.setAttribute(node, "from", this, "from");
		ReflectionUtil.setAttribute(node, "to", this, "to");
		String fc = XMLHelpers.findAttributeValue(node, "from-cardinality");
		fromCardinality = Cardinality.getCardinality(fc);
		String tc = XMLHelpers.findAttributeValue(node, "to-cardinality");
		toCardinality = Cardinality.getCardinality(tc);
	}

	public String getSourceId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(from);
	}

	public String getDestinationId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(to);
	}

	public String getSourcePort() {
		return XMLStringUtil.getAfterSeparatorOrNothing(from);
	}

	public String getDestinationPort() {
		return XMLStringUtil.getAfterSeparatorOrNothing(to);
	}

	public Cardinality getSourceCardinality() {
		return fromCardinality;
	}

	public Cardinality getDestinationCardinality() {
		return toCardinality;
	}

	public ComponentRef getSourceComponent() {
		return chain.getComponent(getSourceId());
	}

	public ComponentRef getDestinationComponent() {
		return chain.getComponent(getDestinationId());
	}

	public Object getSourceReferencedObject() {
		ComponentRef component = getSourceComponent();
		if (component == null)
			return null;
		return component.getReferencedObject();
	}

	public Object getDestinationReferencedObject() {
		ComponentRef component = getDestinationComponent();
		if (component == null)
			return null;
		return component.getReferencedObject();
	}

	public String getSource() {
		return from;
	}

	public String getDestination() {
		return to;
	}

	@Override
	public String toString() {
		return from + " - " + to;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		ComponentRef src = getSourceComponent();
		ComponentRef dst = getDestinationComponent();

		CiliaFlag e1 = CiliaError.checkNotNull(this, src, "binding source");
		CiliaFlag e2 = CiliaError.checkNotNull(this, dst, "binding destination");

		if (src != null && src instanceof AdapterRef) {
			IGenericAdapter ro = ((AdapterRef) src).getReferencedObject();
			if (ro != null) {
				if (ro.getType() == AdapterType.OUT) {
					list.add(new CiliaError("Binding " + this + " has its source connected to an out adapter", this));
				}

				if (ro.getType() == AdapterType.IN && !Strings.isNullOrEmpty(getSourcePort())) {
					list.add(new CiliaError("Binding " + this + " reference an in port but it's linked to an adapter",
							this));
				}
			}
		}

		if (dst != null && dst instanceof AdapterRef) {
			IGenericAdapter ro = ((AdapterRef) dst).getReferencedObject();
			if (ro != null) {
				if (ro.getType() == AdapterType.IN) {
					list.add(new CiliaError("Binding " + this + " has its destination connected to an in adapter", this));
				}

				if (ro.getType() == AdapterType.OUT && !Strings.isNullOrEmpty(getDestinationPort())) {
					list.add(new CiliaError("Binding " + this + " reference an out port but it's linked to an adapter",
							this));
				}
			}
		}

		return CiliaFlag.generateTab(list, e1, e2);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		return new ArrayList<Changeset>();
	}

	@Override
	public Object getId() {
		return from + " - " + to;
	}
}
