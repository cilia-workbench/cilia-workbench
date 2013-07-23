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
package fr.liglab.adele.cilia.workbench.common.service.chain;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.parser.ChainFile;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ChainModel;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ParameterRef;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainContentProvider<FileType extends ChainFile<ModelType, ChainType>, ModelType extends ChainModel<ChainType>, ChainType extends Chain>
		extends GenericContentProvider {

	public ChainContentProvider(List<? extends FileType> repo) {

		addRoot(repo);

		for (FileType re : repo) {
			addRelationship(true, repo, re);

			if (re.getModel() != null) {
				for (ChainType c : re.getModel().getChains()) {
					addRelationship(true, re, c);

					for (AdapterRef a : c.getAdapters())
						addRelationship(false, c, a);

					for (MediatorRef m : c.getMediators()) {
						addRelationship(false, c, m);

						for (ParameterRef p : m.getSchedulerParameters())
							addRelationship(false, m, p);
						for (ParameterRef p : m.getProcessorParameters())
							addRelationship(false, m, p);
						for (ParameterRef p : m.getDispatcherParameters())
							addRelationship(false, m, p);

						// TODO update this later

						/*
						 * // Specification specific part. Ignored by //
						 * implementations... if (m instanceof MediatorSpecRef)
						 * { for (PropertyConstraint p : ((MediatorSpecRef)
						 * m).getConstraints()) addRelationship(false, m, p); }
						 */
					}

					for (Binding b : c.getBindings()) {
						addRelationship(false, c, b);

						// TODO here... mecanique pour les bindings...
					}
				}
			}
		}
	}

	public Object getBinding(Object src, Object dst) {
		// TODO Auto-generated method stub
		// TODO here... mecanique pour les bindings...
		return null;
	}
}
