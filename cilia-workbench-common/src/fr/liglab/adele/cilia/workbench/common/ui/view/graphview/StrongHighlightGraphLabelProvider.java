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

import org.eclipse.swt.graphics.Color;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;

/**
 * GraphLabelProvider used to highlight specific nodes in the graph. Only nodes
 * selected using the {@link NodeSelector} are highlighted. Others are displayed
 * with the default painting renderer.
 * 
 * @author Etienne Gandrille
 */
public class StrongHighlightGraphLabelProvider extends GraphLabelProvider {

	private final NodeSelector nodeSelector;

	public StrongHighlightGraphLabelProvider(GenericContentProvider contentProvider, NodeSelector nodeSelector) {
		super(contentProvider);
		this.nodeSelector = nodeSelector;
	}

	public StrongHighlightGraphLabelProvider(GenericContentProvider contentProvider, GraphTextLabelProvider graphTextLabelProvider, NodeSelector nodeSelector) {
		super(contentProvider, graphTextLabelProvider);
		this.nodeSelector = nodeSelector;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return getNodeColorInternal(entity);
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return getNodeColorInternal(entity);
	}

	@Override
	public Color getBorderColor(Object entity) {
		return getBorderColorInternal(entity);
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return getBorderColorInternal(entity);
	}

	private Color getNodeColorInternal(Object entity) {
		if (nodeSelector.isSelectedNode(entity))
			return getConfig().getNodeStrongHighlightColor();
		else
			return getConfig().getNodeColor();
	}

	private Color getBorderColorInternal(Object entity) {
		if (nodeSelector.isSelectedNode(entity))
			return getConfig().getNodeBorderHighlightColor();
		else
			return getConfig().getNodeBorderColor();
	}
}
