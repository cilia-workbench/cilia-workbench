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
package fr.liglab.adele.cilia.workbench.designer.service.specreposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.spec.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Port;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecModel;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;

/**
 * Content provider used by the spec repository.
 * 
 * @author Etienne Gandrille
 */
public class SpecContentProvider extends GenericContentProvider {

	/**
	 * Initialize maps from model.
	 */
	public SpecContentProvider(List<SpecFile> root) {

		addRoot(root);

		for (SpecFile file : root) {

			addRelationship(root, file);

			SpecModel model = file.getModel();
			if (model != null) {
				for (MediatorSpec spec : model.getMediatorSpecs()) {
					addRelationship(file, spec);

					Scheduler scheduler = spec.getScheduler();
					if (addRelationship(spec, scheduler))
						for (Parameter p : scheduler.getParameters())
							addRelationship(scheduler, p);

					Processor processor = spec.getProcessor();
					if (addRelationship(spec, processor))
						for (Parameter p : processor.getParameters())
							addRelationship(processor, p);

					Dispatcher dispatcher = spec.getDispatcher();
					if (addRelationship(spec, dispatcher))
						for (Parameter p : dispatcher.getParameters())
							addRelationship(dispatcher, p);

					for (Property property : spec.getProperties())
						addRelationship(spec, property);

					for (Port port : spec.getPorts())
						addRelationship(spec, port);
				}
			}
		}
	}
}
