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
package fr.liglab.adele.cilia.workbench.designer.view.specrepositoryview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecModel;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class CreateMediatorHandler extends SpecHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = ViewUtil.getShell(event);

		// Finds specfile
		Object element = getFirstSelectedElementInRepositoryView(event);
		if (!(element instanceof SpecFile)) {
			MessageDialog.openError(shell, "Error", "Please select a spec file first");
			return null;
		}
		SpecFile specFile = (SpecFile) element;

		// Finds model file
		if (specFile.getModel() == null) {
			MessageDialog.openError(shell, "Error", "Can't add a mediator in a non valid file");
			return null;
		}
		SpecModel modelFile = specFile.getModel();

		NewMediatorDialog dialog = new NewMediatorDialog(shell);

		if (dialog.open() == Window.OK) {
			String id = dialog.getId();
			String namespace = dialog.getNamespace();

			SpecRepoService repoService = SpecRepoService.getInstance();
			return repoService.createMediatorSpec(modelFile, id, namespace);
		}

		return null;
	}
}
