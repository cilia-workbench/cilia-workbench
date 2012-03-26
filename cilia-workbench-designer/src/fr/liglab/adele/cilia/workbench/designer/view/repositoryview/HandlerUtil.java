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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;

/**
 * A few static methods for handlers implementation.
 * 
 * @author Etienne Gandrille
 */
public abstract class HandlerUtil {

	/**
	 * Handler for creating a file in a repository. Check if the new file name is allowed, and creates a new file in a
	 * repository with the associated default content.
	 * 
	 * @param event
	 * @param repoService
	 * @return
	 * @throws ExecutionException
	 */
	public static Object CreateFileHandler(ExecutionEvent event, final AbstractRepoService<?> repoService)
			throws ExecutionException {

		// Validator
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				return repoService.isNewFileNameAllowed(newText);
			}
		};

		// Dialog creation
		InputDialog dialog = new InputDialog(ViewUtil.getShell(event), "File creation",
				"Please give a name for the new file.", repoService.ext, validator);

		if (dialog.open() == Window.OK) {
			String fileName = dialog.getValue();
			repoService.createFile(fileName);
		}

		return null;
	}
}
