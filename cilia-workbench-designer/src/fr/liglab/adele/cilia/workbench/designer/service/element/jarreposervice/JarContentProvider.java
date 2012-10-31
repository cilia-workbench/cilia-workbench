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
package fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.AdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CollectorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.DispatcherImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.ParameterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.ProcessorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.PropertyImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SchedulerImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SenderImplem;

/**
 * Content provider used by the jar repository.
 * 
 * @author Etienne Gandrille
 */
public class JarContentProvider extends GenericContentProvider {

	public JarContentProvider(List<CiliaJarFile> model) {

		addRoot(model);

		for (CiliaJarFile bundle : model) {

			addRelationship(true, model, bundle);
			CiliaJarModel ipojo = bundle.getModel();

			if (ipojo != null) {
				for (MediatorImplem mc : ipojo.getMediatorComponents()) {
					addRelationship(true, bundle, mc);

					if (mc.getSpec() != null)
						addRelationship(true, mc, mc.getSpec());

					SchedulerImplem scheduler = mc.getScheduler();
					if (scheduler != null)
						addRelationship(true, mc, scheduler);
					else
						addRelationship(true, mc, new FakeElement(mc.getSchedulerID().toString(), "Unknown scheduler "
								+ mc.getSchedulerID().toString(), SchedulerImplem.class));

					ProcessorImplem processor = mc.getProcessor();
					if (processor != null)
						addRelationship(true, mc, processor);
					else
						addRelationship(true, mc, new FakeElement(mc.getProcessorID().toString(), "Unknown processor "
								+ mc.getProcessorID().toString(), SchedulerImplem.class));

					DispatcherImplem dispatcher = mc.getDispatcher();
					if (dispatcher != null)
						addRelationship(true, mc, dispatcher);
					else
						addRelationship(true, mc, new FakeElement(mc.getDispatcherID().toString(),
								"Unknown dispatcher " + mc.getDispatcherID().toString(), SchedulerImplem.class));

					for (IPort p : mc.getPorts())
						addRelationship(true, mc, p);

					for (PropertyImplem p : mc.getProperties())
						addRelationship(true, mc, p);
				}

				for (SchedulerImplem s : ipojo.getSchedulers()) {
					addRelationship(true, bundle, s);
					for (ParameterImplem param : s.getParameters())
						addRelationship(true, s, param);
				}

				for (ProcessorImplem p : ipojo.getProcessors()) {
					addRelationship(true, bundle, p);
					for (ParameterImplem param : p.getParameters())
						addRelationship(true, p, param);
				}

				for (DispatcherImplem d : ipojo.getDispatchers()) {
					addRelationship(true, bundle, d);
					for (ParameterImplem param : d.getParameters())
						addRelationship(true, d, param);
				}

				for (CollectorImplem c : ipojo.getCollectors()) {
					addRelationship(true, bundle, c);
					for (ParameterImplem param : c.getParameters())
						addRelationship(true, c, param);
				}

				for (SenderImplem s : ipojo.getSenders()) {
					addRelationship(true, bundle, s);
					for (ParameterImplem param : s.getParameters())
						addRelationship(true, s, param);
				}

				for (AdapterImplem a : ipojo.getAdapters())
					addRelationship(true, bundle, a);
			}
		}
	}
}
