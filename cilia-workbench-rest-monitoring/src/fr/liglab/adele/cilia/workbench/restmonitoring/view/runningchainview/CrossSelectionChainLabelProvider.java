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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import org.eclipse.swt.graphics.Color;

import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;

/**
 * 
 * @author Etienne Gandrille
 */
public class CrossSelectionChainLabelProvider extends PlatformChainLabelProvider {

	private final String componentId;

	public CrossSelectionChainLabelProvider(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return getPaintingColor(entity);
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return getPaintingColor(entity);
	}

	private Color getPaintingColor(Object entity) {
		if (entity != null && entity instanceof ComponentRef) {
			ComponentRef compoRef = (ComponentRef) entity;
			String compoId = compoRef.getId();
			if (needStrongHighlight(compoId))
				return getConfig().getNodeStrongHighlightColor();
			else
				return getConfig().getNodeColor();
		} else
			return getConfig().getNodeColor();
	}

	private boolean needStrongHighlight(String compoId) {
		if (compoId != null && compoId.startsWith(componentId))
			return true;
		return false;
	}
}
