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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;

/**
 * NewMediatorInstanceWindow.
 * 
 * @author Etienne Gandrille
 */
public class NewMediatorInstanceWindow extends NewComponentInstanceWindow {

	/**
	 * Instantiates a new new mediator instance window.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param chain
	 *            the chain
	 */
	protected NewMediatorInstanceWindow(Shell parentShell, Chain chain) {
		super("mediator", parentShell, chain);
		componentsId = JarRepoService.getInstance().getMediatorsId();
	}

	@Override
	protected String checkValidValues(String id, NameNamespaceID type) {
		return chain.isNewMediatorInstanceAllowed(id, type);
	}
}
