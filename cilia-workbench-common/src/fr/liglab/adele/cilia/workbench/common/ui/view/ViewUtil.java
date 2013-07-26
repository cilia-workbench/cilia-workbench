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
package fr.liglab.adele.cilia.workbench.common.ui.view;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * Utility class for packaging a few static methods related to views management.
 * 
 * @author Etienne Gandrille
 */
public class ViewUtil {

	public static IViewPart findViewWithId(WorkbenchPart part, String viewId) {
		return findViewWithId(part.getSite().getWorkbenchWindow(), viewId);
	}

	public static IViewPart findViewWithId(ExecutionEvent event, String viewId) {
		return findViewWithId(HandlerUtil.getActiveWorkbenchWindow(event), viewId);
	}

	public static IViewPart findViewWithId(IWorkbenchWindow workbenchWindow, String viewId) {
		IViewReference[] views = workbenchWindow.getActivePage().getViewReferences();
		for (IViewReference view : views)
			if (view.getId().equals(viewId))
				return view.getView(true);

		throw new RuntimeException("view with id " + viewId + " not found.");
	}

	public static Shell getShell(ExecutionEvent event) {
		return HandlerUtil.getActiveWorkbenchWindow(event).getShell();
	}

	public static Shell getShell(DoubleClickEvent event) {
		return event.getViewer().getControl().getShell();
	}

	public static Shell getShell(Control control) {
		while (control != null && !(control instanceof Shell))
			control = control.getParent();

		if (control == null)
			return null;

		return (Shell) control;
	}

	/**
	 * An handler, which opens a small dialog with a Non Yet Implemented
	 * message.
	 * 
	 * @param event
	 * @return
	 * @throws ExecutionException
	 */
	public static Object notYetImplementedHandler(ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(ViewUtil.getShell(event), "Not yet implemented", event.getCommand().getId());
		return null;
	}

	public static Object notYetImplementedHandler(Shell parentShell) {
		MessageDialog.openInformation(parentShell, "Not yet implemented", "Not yet implemented");
		return null;
	}
}
