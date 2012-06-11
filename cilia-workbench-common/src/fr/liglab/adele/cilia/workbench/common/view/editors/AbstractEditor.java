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
package fr.liglab.adele.cilia.workbench.common.view.editors;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * Base class for implementing editors, such as list editors and map editors.
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractEditor {

	/** The add label. */
	protected final String addLabel = "Add";

	/** The remove label. */
	protected final String removeLabel = "Remove";	
	
	/** The JFace viewer */
	protected StructuredViewer jFaceViewer;

	/** The parent Shell */
	protected Shell shell;
	
	/** The main composite for this widget. */
	protected Composite widgetComposite;
	
	public AbstractEditor(Composite parent) {
		this.shell = parent.getShell();
	}
	
	/**
	 * Sets the comparator, for sorting values in the table.
	 *
	 * @param comparator the new comparator
	 */
	public void setComparator(ViewerComparator comparator) {
		jFaceViewer.setComparator(comparator);
		refresh();
	}
	
	/**
	 * Gets the default comparator.
	 *
	 * @return the default comparator
	 */
	protected abstract ViewerComparator getDefaultComparator() ;
	
	/**
	 * Gets the composite.
	 * 
	 * @return the composite
	 */
	public Control getComposite() {
		return widgetComposite;
	}

	/**
	 * Repaints the widget.
	 */
	public abstract void refresh();
}
