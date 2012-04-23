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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;

/**
 * Content provider used by the DSCilia repository.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaContentProvider extends GenericContentProvider {

	/**
	 * Initialize maps from model.
	 */
	public DsciliaContentProvider(List<DsciliaFile> repo) {

		addRoot(repo);

		for (DsciliaFile re : repo) {
			addRelationship(repo, re);

			if (re.getModel() != null) {
				for (Chain c : re.getModel().getChains()) {
					addRelationship(re, c);
					
					for (AdapterInstance a : c.getAdapters())
						addRelationship(c, a);
					
					for (MediatorInstance m : c.getMediators())
						addRelationship(c, m);
					
					for (Binding b : c.getBindings())
						addRelationship(c, b);
				}
			}
		}
	}
}
