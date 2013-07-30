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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.runtimetorefarch.RuntimeToRefArchChecker;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class AdapterInstanceRef extends AdapterRef {

	private final String ADAPTER_VALID_STATE = "VALID";
	private final String state;
	private final PlatformID platformId;
	private final String chainId;

	public AdapterInstanceRef(String adapterID, NameNamespaceID adapterTypeID, String state, PlatformID platformId, String chainId) {
		super(adapterID, adapterTypeID);
		this.state = state;
		this.platformId = platformId;
		this.chainId = chainId;
	}

	@Override
	public PlatformChain getChain() {
		return PlatformRepoService.getInstance().getPlatformChain(platformId, chainId);
	}

	@Override
	protected ComponentRepoService<?, ?> getComponentRepoService() {
		return CiliaJarRepoService.getInstance();
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);
		AdapterInstanceRef newRef = (AdapterInstanceRef) other;
		retval.addAll(MergeUtil.computeUpdateChangeset(newRef, this, "state"));
		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab1 = super.getErrorsAndWarnings();
		List<CiliaFlag> tab2 = null;
		CiliaFlag e1 = null;

		// iPOJO running state checking
		if (!ADAPTER_VALID_STATE.equals(state))
			e1 = new CiliaError("Invalid state for adapter " + getId() + " (" + state + ")", this);

		// reference architecture checking
		tab2 = RuntimeToRefArchChecker.checkComponent(this);

		return CiliaFlag.generateTab(tab1, tab2, e1);
	}
}
