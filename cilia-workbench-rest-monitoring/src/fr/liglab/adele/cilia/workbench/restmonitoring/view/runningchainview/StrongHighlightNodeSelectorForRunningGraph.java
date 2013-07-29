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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.NodeSelector;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class StrongHighlightNodeSelectorForRunningGraph implements NodeSelector {

	private final String componentId;
	private final NameNamespaceID refArchId;

	public StrongHighlightNodeSelectorForRunningGraph(NameNamespaceID refArchId, String componentId) {
		this.refArchId = refArchId;
		this.componentId = componentId;
	}

	@Override
	public boolean isSelectedNode(Object nodeObject) {
		if (nodeObject != null && nodeObject instanceof ComponentRef) {
			ComponentRef compoRef = (ComponentRef) nodeObject;
			try {
				PlatformChain chain = (PlatformChain) compoRef.getChain();
				if (chain.getComponentInReferenceArchitecture(compoRef).getId().equals(componentId))
					return true;
			} catch (Exception e) {
				// do nothing
			}
		}

		return false;
	}
}
