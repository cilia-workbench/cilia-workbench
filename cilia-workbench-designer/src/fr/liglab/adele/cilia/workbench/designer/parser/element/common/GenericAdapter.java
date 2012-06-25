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
package fr.liglab.adele.cilia.workbench.designer.parser.element.common;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class GenericAdapter extends NameNamespace implements IGenericAdapter, Identifiable,
		ErrorsAndWarningsFinder {

	/**
	 * Sub element is a collector or a sender, depending on the
	 * {@link AdapterType}.
	 * 
	 * @param subElement
	 *            the new sub element
	 */
	protected abstract void setSubElement(String subElement);

	/**
	 * Sub element is a collector or a sender, depending on the
	 * {@link AdapterType}.
	 * 
	 * @return the sub element
	 */
	protected abstract String getSubElement();

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] flagsTab = super.getErrorsAndWarnings();

		CiliaError e1 = CiliaError.checkStringNotNullOrEmpty(this, getSubElement(), "sub element");

		return CiliaFlag.generateTab(flagsTab, e1);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}
}