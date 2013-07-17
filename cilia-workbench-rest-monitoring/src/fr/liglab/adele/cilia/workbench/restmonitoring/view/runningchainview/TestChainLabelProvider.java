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

import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.COLOR;

/**
 * 
 * @author Etienne Gandrille
 */
public class TestChainLabelProvider extends PlatformChainLabelProvider {

	@Override
	public Color getBackgroundColour(Object entity) {
		return COLOR.RED.getColor();
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return COLOR.RED.getColor();
	}
}
