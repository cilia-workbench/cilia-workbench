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
package fr.liglab.adele.cilia.workbench.designer.view.dsciliarepositoryview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

/**
 * DeleteChainHandler.
 * 
 * @author Etienne Gandrille
 */
public class DeleteChainHandler extends DsciliaViewHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object object = getFirstSelectedElementInRepositoryView(event);
		if (object != null && object instanceof Chain) {
			Chain chain = (Chain) object;
			boolean result = MessageDialog.openConfirm(ViewUtil.getShell(event), "Confirmation required",
					"Do you want to delete " + chain.getId() + "?");
			if (result == true)
				DsciliaRepoService.getInstance().deleteChain(chain);
		} else {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "You must select a chain first.");
		}
		return null;
	}
}
