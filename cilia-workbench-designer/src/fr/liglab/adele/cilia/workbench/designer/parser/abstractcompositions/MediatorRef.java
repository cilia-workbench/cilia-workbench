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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericMediator;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class MediatorRef extends ComponentRef {

	public MediatorRef(Node node, Chain parent) throws CiliaException {
		super(node, parent);
	}

	public abstract IGenericMediator getReferencedObject();

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getIncommingBindings().length == 0)
			e1 = new CiliaError(this + " doesn't have an incomming binding", this);

		if (getOutgoingBindings().length == 0)
			e2 = new CiliaError(this + " doesn't have an outgoing binding", this);

		return CiliaFlag.generateTab(tab, e1, e2);
	}

}
