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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class XMLBinding extends Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_NODE_NAME = "binding";

	public static final String XML_FROM_ATTR = "from";
	public static final String XML_TO_ATTR = "to";

	protected final NameNamespaceID chainId;

	public XMLBinding(Node node, NameNamespaceID chainId) throws CiliaException {
		super(XMLHelpers.findAttributeValue(node, XML_FROM_ATTR), XMLHelpers.findAttributeValue(node, XML_TO_ATTR));
		this.chainId = chainId;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaFlag e1 = null;
		CiliaFlag e2 = null;
		CiliaFlag e3 = null;
		CiliaFlag e4 = null;

		ComponentRef src = getSourceComponentRef();
		ComponentRef dst = getDestinationComponentRef();

		if (!Strings.isNullOrEmpty(getSourcePort()) && getSourceComponentDefinition() != null)
			if (!getSourceComponentDefinition().hasOutPort(getSourcePort()))
				e1 = new CiliaError("Binding " + this + " source port is undefined in " + getSourceComponentDefinition(), this);

		if (!Strings.isNullOrEmpty(getDestinationPort()) && getDestinationComponentDefinition() != null)
			if (!getDestinationComponentDefinition().hasInPort(getDestinationPort()))
				e2 = new CiliaError("Binding " + this + " destination port is undefined in " + getDestinationComponentDefinition(), this);

		if (src != null && src instanceof AdapterRef) {
			Adapter ro = ((AdapterRef) src).getReferencedComponentDefinition();
			if (ro != null) {
				if (ro.getType() == AdapterType.OUT) {
					e3 = new CiliaError("Binding " + this + " has its source connected to an out adapter", this);
				}
			}
		}

		if (dst != null && dst instanceof AdapterRef) {
			Adapter ro = ((AdapterRef) dst).getReferencedComponentDefinition();
			if (ro != null) {
				if (ro.getType() == AdapterType.IN) {
					e4 = new CiliaError("Binding " + this + " has its destination connected to an in adapter", this);
				}
			}
		}

		return CiliaFlag.generateTab(tab, e1, e2, e3, e4);
	}
}
