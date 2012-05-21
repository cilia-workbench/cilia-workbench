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
package fr.liglab.adele.cilia.workbench.designer.view.abstractcompositionsview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AbstractCompositionFile;

/**
 * CreateChainHandler.
 * 
 * @author Etienne Gandrille
 */
public class CreateChainHandler extends DsciliaViewHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Gets the dscilia file first
		Object object = getFirstSelectedElementInRepositoryView(event);
		if (!(object instanceof AbstractCompositionFile)) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Please select a dscilia file first.");
			return null;
		}
		final AbstractCompositionFile repo = (AbstractCompositionFile) object;
		if (repo.getModel() == null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error",
					"Dscilia file must be in a valid state. Please check xml.");
			return null;
		}

		// Validator
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				return null;
				// TODO validates nothing here !
				// return
				// AbstractCompositionsRepoService.getInstance().isNewChainNameAllowed(newText);
			}
		};

		// Dialog creation
		InputDialog dialog = new InputDialog(ViewUtil.getShell(event), "Chain creation",
				"Please give a name for the new chain.", "", validator);

		if (dialog.open() == Window.OK) {
			String chainName = dialog.getValue();

			// TODO
			// AbstractCompositionsRepoService.getInstance().createChain(repo,
			// chainName);
		}

		return null;
	}
}
