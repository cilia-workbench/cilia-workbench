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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AdapterRef extends ComponentRef {

	public AdapterRef(String componentID, NameNamespaceID referencedComponentID) {
		super(componentID, referencedComponentID);
	}

	public Adapter getReferencedComponentDefinition() {
		NameNamespaceID id = getReferencedComponentDefinitionID();
		return componentRepo.getAdapter(id);
	}

	public AdapterType getType() {
		if (getReferencedComponentDefinition() != null)
			return getReferencedComponentDefinition().getType();
		else
			return null;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getType() != null) {

			switch (getType()) {
			case IN:
				if (getIncommingBindings().length != 0)
					e1 = new CiliaError(this + " shouldn't have an incomming binding", this);
				if (getOutgoingBindings().length == 0)
					e2 = new CiliaError(this + " doesn't have an outgoing binding", this);
				break;
			case OUT:
				if (getIncommingBindings().length == 0)
					e1 = new CiliaError(this + " doesn't have an incomming binding", this);
				if (getOutgoingBindings().length != 0)
					e2 = new CiliaError(this + " shouldn't have an outgoing binding", this);
				break;
			case INOUT:
				if (getIncommingBindings().length == 0)
					e1 = new CiliaError(this + " doesn't have an incomming binding", this);
				if (getOutgoingBindings().length == 0)
					e2 = new CiliaError(this + " doesn't have an outgoing binding", this);
				break;
			}
		}

		return CiliaFlag.generateTab(tab, e1, e2);
	}
}
