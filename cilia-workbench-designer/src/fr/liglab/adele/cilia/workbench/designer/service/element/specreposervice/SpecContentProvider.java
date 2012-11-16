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
package fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.parser.element.IPort;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.DispatcherSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.PropertySpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.ParameterSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.ProcessorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SchedulerSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SpecModel;

/**
 * Content provider used by the spec repository.
 * 
 * @author Etienne Gandrille
 */
public class SpecContentProvider extends GenericContentProvider {

	public SpecContentProvider(List<SpecFile> root) {

		addRoot(root);

		for (SpecFile file : root) {

			addRelationship(true, root, file);

			SpecModel model = file.getModel();
			if (model != null) {
				for (MediatorSpec spec : model.getMediatorSpecs()) {
					addRelationship(true, file, spec);

					SchedulerSpec scheduler = spec.getScheduler();
					if (addRelationship(true, spec, scheduler))
						for (ParameterSpec p : scheduler.getParameters())
							addRelationship(true, scheduler, p);

					ProcessorSpec processor = spec.getProcessor();
					if (addRelationship(true, spec, processor))
						for (ParameterSpec p : processor.getParameters())
							addRelationship(true, processor, p);

					DispatcherSpec dispatcher = spec.getDispatcher();
					if (addRelationship(true, spec, dispatcher))
						for (ParameterSpec p : dispatcher.getParameters())
							addRelationship(true, dispatcher, p);

					for (IPort port : spec.getPorts())
						addRelationship(true, spec, port);

					for (PropertySpec property : spec.getProperties())
						addRelationship(true, spec, property);
				}
			}
		}
	}
}
