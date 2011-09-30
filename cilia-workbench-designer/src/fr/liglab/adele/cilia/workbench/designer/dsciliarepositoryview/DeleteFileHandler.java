/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

/**
 * DeleteFileHandler.
 */
public class DeleteFileHandler extends CommonHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object object = getFirstSelectedElementInRepositoryView(event);
		if (object != null && object instanceof RepoElement) {
			RepoElement repo = (RepoElement) object;
			boolean result = MessageDialog.openConfirm(getShell(event), "Confirmation required", "Do you want to delete " + repo.getFilePath() + "?");
			if (result == true)
				DsciliaRepoService.getInstance().deleteRepoElement(repo);
		} else {
			MessageDialog.openError(getShell(event), "Error", "You must select a dscila file first.");
		}
		return null;
	}
}
