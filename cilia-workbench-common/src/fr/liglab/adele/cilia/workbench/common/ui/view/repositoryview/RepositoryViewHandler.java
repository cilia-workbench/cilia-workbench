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
package fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.service.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;

/**
 * Base class for implementing handlers dealing with repositories. This class
 * should be subclassed, to provide the view id using the constructor.
 * 
 * @author Etienne Gandrille
 */
public abstract class RepositoryViewHandler extends AbstractHandler {

	/** ViewID, used to find the repository view. */
	private final String viewID;

	public RepositoryViewHandler(String viewID) {
		this.viewID = viewID;
	}

	protected RepositoryView<?, ?> getRepositoryView(ExecutionEvent event) {
		return (RepositoryView<?, ?>) ViewUtil.findViewWithId(event, viewID);
	}

	protected AbstractRepoService<?, ?> getRepoService(ExecutionEvent event) {
		return getRepositoryView(event).getRepoService();
	}

	protected Object getFirstSelectedElementInRepositoryView(ExecutionEvent event) {
		RepositoryView<?, ?> view = (RepositoryView<?, ?>) getRepositoryView(event);
		return view.getFirstSelectedElement();
	}

	public Object deleteFile(ExecutionEvent event) throws ExecutionException {

		// Important ! must be recorded in a variable to prevent focus problems.
		RepositoryView<?, ?> view = (RepositoryView<?, ?>) getRepositoryView(event);
		Object object = view.getFirstSelectedElement();

		if (object != null && object instanceof AbstractFile<?>) {
			AbstractFile<?> file = (AbstractFile<?>) object;
			boolean result = MessageDialog.openConfirm(ViewUtil.getShell(event), "Confirmation required",
					"Do you want to delete " + file.getFile() + "?");
			if (result == true) {
				AbstractRepoService<?, ?> repoService = view.getRepoService();
				repoService.deleteRepoElement(file);
			}
		}
		return false;
	}

	/**
	 * Creates a file in a repository. Check if the new file name is allowed,
	 * and creates a new file in a repository with the associated default
	 * content.
	 * 
	 * @param the
	 *            handler event
	 * @param repoService
	 * @return
	 * @throws ExecutionException
	 */
	public Object createFile(final ExecutionEvent event) throws ExecutionException {

		// Important ! must be recorded in a variable to prevent focus problems.
		final AbstractRepoService<?, ?> repoService = getRepoService(event);

		// Validator
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				return repoService.isNewFileNameAllowed(newText);
			}
		};

		// Dialog creation
		InputDialog dialog = new InputDialog(ViewUtil.getShell(event), "File creation",
				"Please give a name for the new file.", getRepoService(event).ext, validator);

		if (dialog.open() == Window.OK) {
			String fileName = dialog.getValue();
			return repoService.createFile(fileName);
		}

		return null;
	}
}
