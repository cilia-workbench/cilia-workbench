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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Port implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable {

	private String name;

	public Port(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, "name", this, "name");
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Object getId() {
		return name;
	}

	public abstract boolean isInPort();

	public abstract boolean isOutPort();

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		return CiliaFlag.generateTab(e);
	}
}
