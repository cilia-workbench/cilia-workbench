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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * Represents a link to a mediator specification.
 * 
 * @author Etienne Gandrille
 */
public class SuperMediator extends NameNamespaceID implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	public SuperMediator(String name, String namespace) {
		super(name, namespace);
	}

	public MediatorSpec getMediatorSpec() {
		return SpecRepoService.getInstance().getMediatorSpec(this);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {

		CiliaError e = null;

		MediatorSpec spec = getMediatorSpec();
		if (spec == null)
			e = new CiliaError("Can't find mediator spec " + getNamespace() + "." + getName(), this);

		return CiliaFlag.generateTab(e);
	}
}
