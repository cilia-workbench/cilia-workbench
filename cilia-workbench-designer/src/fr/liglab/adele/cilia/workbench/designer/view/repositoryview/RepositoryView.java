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

import java.util.ArrayList;
import java.util.List;

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

import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.IRepoServiceListener;

/**
 * RepositoryView.
 * 
 * @author Etienne Gandrille
 */
public abstract class RepositoryView<ModelType> extends ViewPart implements IRepoServiceListener {

	/** Main viewer. */
	protected TreeViewer viewer;

	/** The view internal model. */
	private List<ModelType> model = new ArrayList<ModelType>();

	/** Message area used to display last model reload date. */
	protected Label messageArea;

	/** The message area prefix. */
	protected final String messageAreaPrefix = "Repository directory: ";

	protected final AbstractRepoService<ModelType> repoService;

	public RepositoryView(AbstractRepoService<ModelType> repoService) {
		this.repoService = repoService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setAutoExpandLevel(2);

		// Label
		messageArea = new Label(parent, SWT.WRAP);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Register repository listener
		repoService.registerListener(this);

		// Populates view
		refresh();

		// Selection provider
		getSite().setSelectionProvider(viewer);
	}

	/**
	 * Refresh viewer.
	 */
	public void refresh() {
		messageArea.setText(computeMessageAreaText());
		model = repoService.getModel();
		viewer.setContentProvider(repoService.getContentProvider());
		viewer.setInput(model);
		viewer.refresh();
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
	protected String getRepositoryDirectory() {
		return repoService.getRepositoryPath();
	}

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
	 * Gets the first element selected in the viewer.
	 * 
	 * @return the element, or null if not found.
	 */
	public Object getFirstSelectedElement() {
		ISelectionService selServ = getSite().getWorkbenchWindow().getSelectionService();
		ISelection sel = selServ.getSelection();
		if (sel != null && sel instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) sel;
			return ts.getFirstElement();
		}

		return null;
	}

	/**
	 * Called when repository changes. Refresh view if needed.
	 * 
	 * @param changes
	 *            the changes
	 */
	@Override
	public void repositoryContentUpdated(Changeset[] changes) {
		for (Changeset change : changes) {
			if (change.getOperation() != Operation.UPDATE) {
				refresh();
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		repoService.unregisterListener(this);
	}
}
