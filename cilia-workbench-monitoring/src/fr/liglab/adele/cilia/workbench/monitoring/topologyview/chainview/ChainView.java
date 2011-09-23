package fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import cilia.monitoring.CiliaUtil;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.GraphContentProvider;

public class ChainView extends ViewPart implements IZoomableWorkbenchPart, ISelectionListener {

	public static final String ID = "cilia.monitoring.topologyview.chainview";
	private GraphViewer viewer;

	public ChainView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		// Selection service notification
		ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
		s.addSelectionListener(TopologyView.viewId, this);

		viewer = new GraphViewer(parent, SWT.BORDER);

		// connexion style must be set at the beginning
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

		// pour obtenir les noeuds atteignables à partir du noeud courant
		viewer.setContentProvider(new GraphContentProvider());

		// content provider
		viewer.setLabelProvider(new NodeElement(null));

		// Model : retourne une liste de noeuds
		viewer.setInput(new Object[0]);

		// Layout
		LayoutAlgorithm layout = setLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();

		fillToolBar();
		
		getSite().setSelectionProvider(viewer);
	}

	private LayoutAlgorithm setLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		layout = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	private void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	private void setModel(ChainReadOnly chain) {
		List<Object> list = new ArrayList<Object>();

		for (Object o : chain.getAdapters()) {
			AdapterReadOnly adapt = (AdapterReadOnly) o;
			list.add(new NodeElement(adapt));
		}

		for (Object o : chain.getMediators()) {
			MediatorReadOnly mediator = (MediatorReadOnly) o;
			list.add(new NodeElement(mediator));
		}

		viewer.setInput(list);
		viewer.refresh();
	}

	
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object element = treeSelection.getFirstElement();

			if (element instanceof ChainReadOnly) {
				ChainReadOnly chain = (ChainReadOnly) element;
				setModel(chain);
			}

			if (element instanceof MediatorReadOnly) {
				MediatorReadOnly mediator = (MediatorReadOnly) element;

				IViewReference[] views = getSite().getPage().getViewReferences();
				for (IViewReference view : views) {
					if (view.getId().equals(TopologyView.viewId)) {
						TopologyView tv = (TopologyView) view.getView(true);
						CiliaContextReadOnly ccro = tv.getModel().getCiliaContextRO();
						ChainReadOnly parent = CiliaUtil.getMediatorParent(ccro, mediator);
						if (parent != null)
							setModel(parent);
					}
				}
			}
		}
	}
}
