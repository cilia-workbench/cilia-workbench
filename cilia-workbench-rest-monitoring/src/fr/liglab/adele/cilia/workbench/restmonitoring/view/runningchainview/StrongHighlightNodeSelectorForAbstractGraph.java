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

import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.NodeSelector;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class StrongHighlightNodeSelectorForAbstractGraph implements NodeSelector {

	private final PlatformID platformID;
	private final String chainName;
	private final String componentId;

	public StrongHighlightNodeSelectorForAbstractGraph(PlatformID platformID, String chainName, String componentId) {
		this.platformID = platformID;
		this.chainName = chainName;
		this.componentId = componentId;
	}

	private PlatformChain getPlatformChain() {
		try {
			return PlatformRepoService.getInstance().getPlatformChain(platformID, chainName);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	private String getIdToBeWatch() {
		try {
			return getPlatformChain().getComponentInReferenceArchitecture(getPlatformChain().getComponent(componentId)).getId();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isSelectedNode(Object nodeObject) {
		if (nodeObject != null && nodeObject instanceof ComponentRef) {
			ComponentRef compoRef = (ComponentRef) nodeObject;
			try {
				if (getIdToBeWatch().equals(compoRef.getId()))
					return true;
			} catch (Exception e) {
				// do nothing
			}
		}
		return false;
	}
}
