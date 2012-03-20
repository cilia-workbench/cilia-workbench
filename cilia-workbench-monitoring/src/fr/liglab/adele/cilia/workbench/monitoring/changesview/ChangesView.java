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
package fr.liglab.adele.cilia.workbench.monitoring.changesview;

import java.util.Collection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;


import fr.liglab.adele.cilia.management.BookMark;
import fr.liglab.adele.cilia.management.monitoring.ChangeSet;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;

/**
 * ChangesView.
 * @author Etienne Gandrille
 */
public class ChangesView extends ViewPart implements ISelectionListener {

	public static final String viewId = "fr.liglab.adele.cilia.workbench.monitoring.changesview";
	
	/** Main viewer. */
	private TableViewer viewer;
	
	public ChangesView() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Selection service notification
		ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
		s.addSelectionListener(TopologyView.viewId, this);

		// Layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = ChangesTableViewer.getViewer(parent);

		// Selection provider
		getSite().setSelectionProvider(viewer);		
	}	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object element = treeSelection.getFirstElement();
			
			// Get monitored element
			Object me = CiliaUtil.getMonitoredElement(element);
			
			// Get Changes
			@SuppressWarnings("rawtypes")
			Collection changes = null;
			if (me != null && me instanceof ChangeSet) {
				ChangeSet cs = (ChangeSet) me;
				changes = cs.getContextualChanges();
			}
			
			// Update view
			if (changes != null)
				viewer.setInput(changes);
			else
				viewer.setInput(new BookMark[0]);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
        ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
        s.removeSelectionListener(this);
        super.dispose();
    }
}
