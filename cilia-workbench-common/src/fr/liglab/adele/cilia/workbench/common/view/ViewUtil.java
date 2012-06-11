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
package fr.liglab.adele.cilia.workbench.common.view;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Utility class for packaging a few static methods related to views management.
 * 
 * @author Etienne Gandrille
 */
public class ViewUtil {

	/**
	 * Finds a view using its id.
	 * 
	 * @param event
	 *            an event
	 * @param viewId
	 *            the view id
	 * @return the view
	 */
	public static IViewPart findViewWithId(ExecutionEvent event, String viewId) {
		IViewReference[] views = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getViewReferences();
		for (IViewReference view : views)
			if (view.getId().equals(viewId))
				return view.getView(true);

		throw new RuntimeException("view with id " + viewId + " not found.");
	}

	/**
	 * Gets the parent shell. Useful for creating modal dialogs.
	 * 
	 * @param event
	 *            the event
	 * @return the shell
	 */
	public static Shell getShell(ExecutionEvent event) {
		return HandlerUtil.getActiveWorkbenchWindow(event).getShell();
	}

	/**
	 * Gets the parent shell. Useful for creating modal dialogs.
	 * 
	 * @param event
	 *            the event
	 * @return the shell
	 */
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
}
