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
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.PropertyConstraint;
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

					for (AdapterRef a : c.getAdapters())
						addRelationship(false, c, a);

					for (MediatorRef m : c.getMediators()) {
						addRelationship(false, c, m);

						for (Parameter p : m.getSchedulerParameters())
							addRelationship(false, m, p);
						for (Parameter p : m.getProcessorParameters())
							addRelationship(false, m, p);
						for (Parameter p : m.getDispatcherParameters())
							addRelationship(false, m, p);

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
