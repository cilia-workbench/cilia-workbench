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

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ParameterRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorInstanceRef extends MediatorRef implements Mergeable {

	private final String MEDIATOR_VALID_STATE = "VALID";
	private final String state;
	private final PlatformID platformId;
	private final String chainId;

	public MediatorInstanceRef(String mediatorID, NameNamespaceID mediatorTypeID, String state, PlatformID platformId, String chainId) {
		super(mediatorID, mediatorTypeID);
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
	public List<ParameterRef> getSchedulerParameters() {
		// TODO getSchedulerParameters in MediatorInstanceRef
		return null;
	}

	@Override
	public List<ParameterRef> getProcessorParameters() {
		// TODO getProcessorParameters in MediatorInstanceRef
		return null;
	}

	@Override
	public List<ParameterRef> getDispatcherParameters() {
		// TODO getDispatcherParameters in MediatorInstanceRef
		return null;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);
		MediatorInstanceRef newRef = (MediatorInstanceRef) other;
		retval.addAll(MergeUtil.computeUpdateChangeset(newRef, this, "state"));
		return retval;
	}

	private MediatorRef getMediatorInReferenceArchitecture() {
		AbstractChain abstractChain = getChain().getRefArchitecture();
		if (abstractChain != null)
			for (MediatorRef refMediator : abstractChain.getMediators())
				if (LinkToRefArchHelper.isLinkBetweenId(refMediator.getId(), getId()))
					return refMediator;
		return null;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		List<CiliaFlag> flagList = new ArrayList<CiliaFlag>();
		for (CiliaFlag flag : tab) {
			flagList.add(flag);
		}

		if (!MEDIATOR_VALID_STATE.equals(state)) {
			flagList.add(new CiliaError("Invalid state for mediator " + getId() + " (" + state + ")", this));
		}

		// Reference architecture checking
		AbstractChain abstractChain = getChain().getRefArchitecture();
		if (abstractChain != null) {
			MediatorRef mira = getMediatorInReferenceArchitecture();
			if (mira == null) {
				flagList.add(new CiliaError("Can't find element in reference architecture for " + getId(), this));
			} else {
				Mediator referenceDefinition = mira.getReferencedComponentDefinition();
				Mediator curDefinition = getReferencedComponentDefinition();
				// null values are checked out of this method
				if (referenceDefinition != null && curDefinition != null) {
					String msg = LinkToRefArchHelper.checkCompatible(referenceDefinition, (MediatorImplem) curDefinition);
					if (msg != null)
						flagList.add(new CiliaError(msg, this));
					else {
						for (String error : LinkToRefArchHelper.checkBindings(mira, this))
							flagList.add(new CiliaError(error, this));
					}
				}
			}
		}

		return CiliaFlag.generateTab(flagList);
	}
}
