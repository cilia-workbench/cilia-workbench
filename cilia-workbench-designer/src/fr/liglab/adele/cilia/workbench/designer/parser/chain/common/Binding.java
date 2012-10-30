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
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter.AdapterType;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_NODE_NAME = "binding";

	public static final String XML_FROM_ATTR = "from";
	public static final String XML_TO_ATTR = "to";

	protected NameNamespaceID chainId;
	private String from;
	private String to;

	public Binding(Node node, NameNamespaceID chainId) throws CiliaException {
		this.chainId = chainId;
		ReflectionUtil.setAttribute(node, XML_FROM_ATTR, this, "from");
		ReflectionUtil.setAttribute(node, XML_TO_ATTR, this, "to");
	}

	protected abstract ChainElement getChain();

	public String getSource() {
		return from;
	}

	public String getDestination() {
		return to;
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

	public ComponentRef getSourceComponent() {
		return getChain().getComponent(getSourceId());
	}

	public ComponentRef getDestinationComponent() {
		return getChain().getComponent(getDestinationId());
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
			IAdapter ro = ((AdapterRef) src).getReferencedObject();
			if (ro != null) {
				if (ro.getType() == AdapterType.OUT) {
					list.add(new CiliaError("Binding " + this + " has its source connected to an out adapter", this));
				}

				if (ro.getType() == AdapterType.IN && !Strings.isNullOrEmpty(getSourcePort())) {
					list.add(new CiliaError("Binding " + this + " reference an in port but it's linked to an adapter", this));
				}
			}
		}

		if (dst != null && dst instanceof AdapterRef) {
			IAdapter ro = ((AdapterRef) dst).getReferencedObject();
			if (ro != null) {
				if (ro.getType() == AdapterType.IN) {
					list.add(new CiliaError("Binding " + this + " has its destination connected to an in adapter", this));
				}

				if (ro.getType() == AdapterType.OUT && !Strings.isNullOrEmpty(getDestinationPort())) {
					list.add(new CiliaError("Binding " + this + " reference an out port but it's linked to an adapter", this));
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
