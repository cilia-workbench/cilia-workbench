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
package fr.liglab.adele.cilia.workbench.restmonitoring.service.platform;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.RunningAdapter;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.RunningBinding;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.RunningMediator;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformContentProvider extends GenericContentProvider {

	public PlatformContentProvider(List<PlatformFile> root) {

		addRoot(root);

		for (PlatformFile file : root) {
			addRelationship(true, root, file);

			PlatformModel model = file.getModel();
			if (model != null) {
				for (PlatformChain pc : model.getChains()) {
					addRelationship(true, file, pc);

					for (RunningMediator mediator : pc.getMediators())
						addRelationship(false, pc, mediator);

					for (RunningAdapter adapter : pc.getAdapters())
						addRelationship(false, pc, adapter);

					for (RunningBinding binding : pc.getBindings())
						addRelationship(false, pc, binding);
				}
			}
		}
	}
}
