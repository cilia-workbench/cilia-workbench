/**
 * Copyright 2012-2013 France Télécom 
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

import java.util.Random;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;

/**
 * 
 * @author Etienne Gandrille
 */
public class MemoryAlgorithm extends AbstractLayoutAlgorithm {

	private ObjectLocatorService olc = ObjectLocatorService.getInstance();

	public MemoryAlgorithm(int styles) {
		super(styles);
	}

	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY,
			double boundsWidth, double boundsHeight) {

		for (InternalNode node : entitiesToLayout) {
			GraphNode gn = (GraphNode) node.getLayoutEntity().getGraphData();
			Object modelObject = gn.getData();
			Point location = null;
			if (modelObject instanceof ComponentRef)
				location = olc.getLocation((ComponentRef) modelObject);
			if (location != null)
				node.setLocation(location.x, location.y);
			else {
				Random random = new Random();
				int x = random.nextInt((int) boundsWidth);
				int y = random.nextInt((int) boundsHeight);
				node.setLocation(x, y);
			}
		}
	}

	protected int getCurrentLayoutStep() {
		return 0;
	}

	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return true;
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
	}

	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width,
			double height) {
	}

	public void setLayoutArea(double x, double y, double width, double height) {
	}
}
