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
package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;

/**
 * RepositoryView.
 */
public abstract class RepositoryView extends ViewPart {

	/** Main viewer. */
	protected TreeViewer viewer;

	/** Message area used to display last model reload date. */
	protected Label messageArea;

	/** The message area prefix. */
	protected final String messageAreaPrefix = "Repository directory: ";
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Label
		messageArea = new Label(parent, SWT.WRAP);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Populates view
		refresh();

		// Selection provider
		getSite().setSelectionProvider(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	/**
	 * Gets the repository directory.
	 * 
	 * @return the repository directory
	 */
	protected abstract String getRepositoryDirectory();

	/**
	 * Computes the message area text.
	 * 
	 * @return the message area text
	 */
	protected String computeMessageAreaText() {
		String dir = getRepositoryDirectory();
		if (dir == null || dir.length() == 0)
			return messageAreaPrefix + "not available";
		else
			return messageAreaPrefix + dir;
	}
	
	/**
	 * refreshes the viewer.
	 */
	protected void refresh() {
		messageArea.setText(computeMessageAreaText());
	}
	
	/**
	 * Gets the first element selected in the viewer.
	 * @return the element, or null if not found.
	 */
	protected Object getFirstSelectedElement() {
		ISelectionService selServ = getSite().getWorkbenchWindow().getSelectionService();
		ISelection sel = selServ.getSelection();
		if (sel != null && sel instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) sel;
			return ts.getFirstElement();
		}
		
		return null;
	}
}
