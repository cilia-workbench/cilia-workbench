package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.workbench.common.view.GraphView;
import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.DsciliaRepositoryView;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;


public class ChainDesignerView extends GraphView {

	/** The View ID. */
	public static final String viewId ="fr.liglab.adele.cilia.workbench.designer.chaindesignerview"; 

	private ContentProvider contentProvider = new ContentProvider();
	
	public ChainDesignerView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		// Registers the instance in the selection service 
		ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
		s.addSelectionListener(DsciliaRepositoryView.viewId, this);
		
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new GraphLabelProvider());
		viewer.setInput(new Object[0]);
	}

	private void setModel(Chain chain) {
		contentProvider.setModel(chain);
		viewer.setInput(chain.getElements());
		viewer.refresh();
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object element = treeSelection.getFirstElement();

			if (element instanceof Chain) {
				Chain chain = (Chain) element;
				setModel(chain);
			}
		}
	}
}
