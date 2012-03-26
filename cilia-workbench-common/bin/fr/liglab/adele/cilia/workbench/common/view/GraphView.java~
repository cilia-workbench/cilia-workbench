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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

public abstract class GraphView extends ViewPart implements IZoomableWorkbenchPart, ISelectionListener {

	/** The viewer. */
	protected GraphViewer viewer;

	public void createPartControl(Composite parent) {
		
		viewer = new GraphViewer(parent, SWT.BORDER);

		// Connection style MUST be set at the beginning
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

		// Layout
		LayoutAlgorithm layout = getLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();

		fillToolBar();
		
		getSite().setSelectionProvider(viewer);
	}
	
	/**
	 * Fill tool bar.
	 */
	protected void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

	}
	
	/**
	 * Sets the viewer layout.
	 *
	 * @return the layout algorithm
	 */
	private LayoutAlgorithm getLayout() {
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
		viewer.getControl().setFocus();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.viewers.IZoomableWorkbenchPart#getZoomableViewer()
	 */
	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}
}
