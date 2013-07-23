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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.common;

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.service.chain.ChainRepoService;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.NewNameNamespaceDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewChainDialog extends NewNameNamespaceDialog {

	private final ChainRepoService<?, ?, ?> repo;

	NewChainDialog(Shell parentShell, ChainRepoService<?, ?, ?> repo) {
		super(parentShell, "New chain", "name", "namespace", true);
		this.repo = repo;
	}

	@Override
	protected String checkValidValue(NameNamespaceID value) {
		return repo.isNewChainNameAllowed(value);
	}
}
