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
package fr.liglab.adele.cilia.workbench.designer.service.chain.common;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.ChainFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.PropertyConstraint;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.AdapterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainModel;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ParameterChain;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainContentProvider<ChainType extends Chain> extends GenericContentProvider {

	public ChainContentProvider(List<? extends ChainFile<? extends ChainModel<ChainType>>> repo) {

		addRoot(repo);

		for (ChainFile<? extends ChainModel<ChainType>> re : repo) {
			addRelationship(true, repo, re);

			if (re.getModel() != null) {
				for (ChainType c : re.getModel().getChains()) {
					addRelationship(true, re, c);

					for (AdapterRef a : c.getAdapters())
						addRelationship(false, c, a);

					for (MediatorRef m : c.getMediators()) {
						addRelationship(false, c, m);

						for (ParameterChain p : m.getSchedulerParameters())
							addRelationship(false, m, p);
						for (ParameterChain p : m.getProcessorParameters())
							addRelationship(false, m, p);
						for (ParameterChain p : m.getDispatcherParameters())
							addRelationship(false, m, p);

						// Specification specific part. Ignored by
						// implementations...
						if (m instanceof MediatorSpecRef) {
							for (PropertyConstraint p : ((MediatorSpecRef) m).getConstraints())
								addRelationship(false, m, p);
						}
					}

					for (Binding b : c.getBindings())
						addRelationship(false, c, b);
				}
			}
		}
	}
}
