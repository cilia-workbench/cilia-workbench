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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.specrepositoryview;

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.NewNameNamespaceDialog;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewMediatorDialog extends NewNameNamespaceDialog {

	public NewMediatorDialog(Shell parentShell) {
		super(parentShell, "New mediator specification", "id", "namespace", false);
	}

	@Override
	protected String checkValidValue(NameNamespaceID value) {
		return SpecRepoService.getInstance().isNewMediatorSpecAllowed(value);
	}
}
