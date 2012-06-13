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
package fr.liglab.adele.cilia.workbench.common.ui.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

/**
 * Base class for implementing graphView, using ZEST framework.
 * 
 * @author Etienne Gandrille
 */
public abstract class GraphView extends ViewPart implements IZoomableWorkbenchPart, ISelectionListener {

	protected GraphViewer viewer;

	public void createPartControl(Composite parent, String viewId) {
		viewer = new GraphViewer(parent, SWT.BORDER);
		viewer.setLayoutAlgorithm(getLayout(), true);
		viewer.applyLayout();
		fillToolBar();
		getSite().setSelectionProvider(viewer);
	}

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
		layout = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	public Object getFirstSelectedElement() {
		ISelectionService selServ = getSite().getWorkbenchWindow().getSelectionService();
		ISelection sel = selServ.getSelection();
		if (sel != null && sel instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) sel;
			return ss.getFirstElement();
		}

		return null;
	}
}
