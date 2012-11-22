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
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ParameterRef;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorInstanceRef extends MediatorRef implements Mergeable {

	private final PlatformChain chain;

	public MediatorInstanceRef(String mediatorID, NameNamespaceID mediatorTypeID, PlatformChain chain) {
		super(mediatorID, mediatorTypeID);
		this.chain = chain;
	}

	@Override
	public Chain getChain() {
		return chain;
	}

	@Override
	protected ComponentRepoService<?, ?> getComponentRepoService() {
		return JarRepoService.getInstance();
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
		return new ArrayList<Changeset>();
	}
}
