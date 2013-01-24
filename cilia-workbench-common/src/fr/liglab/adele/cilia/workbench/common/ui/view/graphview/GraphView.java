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
package fr.liglab.adele.cilia.workbench.common.ui.view.graphview;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;

import fr.liglab.adele.cilia.workbench.common.selectionservice.SelectionService;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;

/**
 * Base class for implementing graphView, using ZEST framework.
 * 
 * @author Etienne Gandrille
 */
public abstract class GraphView extends ViewPart implements IZoomableWorkbenchPart {

	private Shell parentShell;

	private ObjectLocatorService olc = ObjectLocatorService.getInstance();

	protected GraphViewer viewer = null;

	private String viewId = null;

	public void createPartControl(Composite parent, String viewId) {
		this.viewId = viewId;
		viewer = new GraphViewer(parent, SWT.BORDER);
		viewer.setLayoutAlgorithm(getLayout(), true);
		viewer.applyLayout();
		fillToolBar();
		parentShell = ViewUtil.getShell(parent);

		// getSite().setSelectionProvider(viewer);
		SelectionService.getInstance().addSelectionProvider(viewId, viewer);

		// double click listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object element = getFirstSelectedElement();
				if (element != null)
					onDoubleClick(parentShell, element);
			}
		});

		viewer.getGraphControl().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {

				for (Object element : viewer.getNodeElements()) {
					GraphItem item = viewer.findGraphItem(element);
					if (item instanceof GraphNode) {
						GraphNode node = (GraphNode) item;
						Point location = node.getLocation();
						olc.updateLocation(element, location);
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}

	protected abstract void onDoubleClick(Shell parentShell, Object element);

	protected void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}

	private LayoutAlgorithm getLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);

		layout = new MemoryAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);

		return layout;

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void setViewName(String name) {
		setPartName(name);
	}

	public void refresh() {
		viewer.refresh();
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	@Override
	public void dispose() {
		super.dispose();
		SelectionService.getInstance().removeSelectionProvider(viewId);
		viewer = null;
	}

	public Object getFirstSelectedElement() {
		// ISelection sel =
		// getSite().getWorkbenchWindow().getSelectionService().getSelection();
		ISelection sel = viewer.getSelection();
		if (sel != null && sel instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) sel;
			return ss.getFirstElement();
		}

		return null;
	}
}
