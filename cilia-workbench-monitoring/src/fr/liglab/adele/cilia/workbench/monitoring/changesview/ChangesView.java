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

public class ChangesView extends ViewPart implements ISelectionListener {

	/** Main viewer */
	private TableViewer viewer;
	
	public ChangesView() {
	}

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
	
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

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
	
	public void dispose() {
        ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
        s.removeSelectionListener(this);
        super.dispose();
    }
}
