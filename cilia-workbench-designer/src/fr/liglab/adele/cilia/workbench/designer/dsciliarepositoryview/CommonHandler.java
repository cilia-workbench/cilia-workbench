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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;

public abstract class CommonHandler extends AbstractHandler {

	/**
	 * Gets the Repository View.
	 * 
	 * @param event
	 *            the handler event
	 * @return the RepositoryView
	 */
	protected DsciliaRepositoryView getRepositoryView(ExecutionEvent event) {
		String viewId = DsciliaRepositoryView.viewId;
		return (DsciliaRepositoryView) ViewUtil.findViewWithId(event, viewId);
	}

	protected Object getFirstSelectedElementInRepositoryView(ExecutionEvent event) {
		DsciliaRepositoryView view = getRepositoryView(event);
		return view.getFirstSelectedElement();
	}

	/**
	 * Gets the parent shell. Useful for creating modal dialogs.
	 * 
	 * @param event
	 *            the event
	 * @return the shell
	 */
	protected Shell getShell(ExecutionEvent event) {
		return HandlerUtil.getActiveWorkbenchWindow(event).getShell();
	}
}
