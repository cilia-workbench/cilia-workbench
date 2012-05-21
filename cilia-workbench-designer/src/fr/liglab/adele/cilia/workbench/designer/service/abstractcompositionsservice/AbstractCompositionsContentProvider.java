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
package fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;

/**
 * Content provider used by the DSCilia repository.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionsContentProvider extends GenericContentProvider {

	/**
	 * Initialize maps from model.
	 */
	public AbstractCompositionsContentProvider(List<AbstractCompositionFile> repo) {

		addRoot(repo);

		for (AbstractCompositionFile re : repo) {
			addRelationship(true, repo, re);

			if (re.getModel() != null) {
				for (Chain c : re.getModel().getChains()) {
					addRelationship(true, re, c);

					for (AdapterComponent a : c.getAdapters())
						addRelationship(false, c, a);

					for (MediatorComponent m : c.getMediators())
						addRelationship(false, c, m);

					for (Binding b : c.getBindings())
						addRelationship(false, c, b);
				}
			}
		}
	}
}
