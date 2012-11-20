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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryViewHandler;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChainFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainRepositoryHandler<ChainType extends XMLChain> extends RepositoryViewHandler {

	public ChainRepositoryHandler(String viewID) {
		super(viewID);
	}

	protected abstract ChainRepoService<?, ?, ChainType> getRepository();

	public Object createChain(ExecutionEvent event) {

		// Gets the file
		Object object = getFirstSelectedElementInRepositoryView(event);
		if (!(object instanceof XMLChainFile<?>)) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Please select a file.");
			return null;
		}
		final XMLChainFile<?> repo = (XMLChainFile<?>) object;
		if (repo.getModel() == null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "File must be in a valid state. Please check xml.");
			return null;
		}

		// Dialog creation
		NewChainDialog dialog = new NewChainDialog(ViewUtil.getShell(event), getRepository());
		if (dialog.open() == Window.OK) {
			NameNamespaceID nn = dialog.getValue();
			getRepository().createChain(repo, nn);
		}

		return null;
	}

	public Object deleteChain(ExecutionEvent event) throws ExecutionException {
		Object object = getFirstSelectedElementInRepositoryView(event);
		if (object != null && object instanceof Chain) {
			@SuppressWarnings("unchecked")
			ChainType chain = (ChainType) object;
			boolean result = MessageDialog.openConfirm(ViewUtil.getShell(event), "Confirmation required", "Do you want to delete " + chain.getId() + "?");
			if (result == true) {
				getRepository().deleteChain(chain);
			}
		} else {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "You must select a chain first.");
		}
		return null;
	}
}
