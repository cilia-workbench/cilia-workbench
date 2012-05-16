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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.JarPort;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Sender;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;

/**
 * Content provider used by the jar repository.
 * 
 * @author Etienne Gandrille
 */
public class JarContentProvider extends GenericContentProvider {

	/**
	 * Initialize maps from model.
	 */
	public JarContentProvider(List<CiliaJarFile> model) {

		addRoot(model);

		for (CiliaJarFile bundle : model) {

			addRelationship(true, model, bundle);
			CiliaJarModel ipojo = bundle.getModel();

			if (ipojo != null) {
				for (MediatorComponent mc : ipojo.getMediatorComponents()) {
					addRelationship(true, bundle, mc);

					if (mc.getSpec() != null)
						addRelationship(true, mc, mc.getSpec());

					Scheduler scheduler = mc.getScheduler();
					if (scheduler != null)
						addRelationship(true, mc, scheduler);
					else
						addRelationship(true, mc, new FakeElement(mc.getSchedulerID().toString(), "Unknown scheduler "
								+ mc.getSchedulerID().toString(), Scheduler.class));

					Processor processor = mc.getProcessor();
					if (processor != null)
						addRelationship(true, mc, processor);
					else
						addRelationship(true, mc, new FakeElement(mc.getProcessorID().toString(), "Unknown processor "
								+ mc.getProcessorID().toString(), Scheduler.class));

					Dispatcher dispatcher = mc.getDispatcher();
					if (dispatcher != null)
						addRelationship(true, mc, dispatcher);
					else
						addRelationship(true, mc, new FakeElement(mc.getDispatcherID().toString(),
								"Unknown dispatcher " + mc.getDispatcherID().toString(), Scheduler.class));

					for (JarPort p : mc.getPorts())
						addRelationship(true, mc, p);

					for (Property p : mc.getProperties())
						addRelationship(true, mc, p);
				}

				for (Scheduler s : ipojo.getSchedulers()) {
					addRelationship(true, bundle, s);
					for (Parameter param : s.getParameters())
						addRelationship(true, s, param);
				}

				for (Processor p : ipojo.getProcessors()) {
					addRelationship(true, bundle, p);
					for (Parameter param : p.getParameters())
						addRelationship(true, p, param);
				}

				for (Dispatcher d : ipojo.getDispatchers()) {
					addRelationship(true, bundle, d);
					for (Parameter param : d.getParameters())
						addRelationship(true, d, param);
				}

				for (Collector c : ipojo.getCollectors()) {
					addRelationship(true, bundle, c);
					for (Parameter param : c.getParameters())
						addRelationship(true, c, param);
				}

				for (Sender s : ipojo.getSenders()) {
					addRelationship(true, bundle, s);
					for (Parameter param : s.getParameters())
						addRelationship(true, s, param);
				}

				for (Adapter a : ipojo.getAdapters())
					addRelationship(true, bundle, a);
			}
		}
	}
}
