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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview;

import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.common.ChainRepositoryHandler;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class DSCiliaViewHandler extends ChainRepositoryHandler<DSCiliaChain> {

	public DSCiliaViewHandler() {
		super(DSCiliaRepositoryView.VIEW_ID);
	}

	@Override
	protected ChainRepoService<?, ?, DSCiliaChain> getRepository() {
		return DSCiliaRepoService.getInstance();
	}
}
