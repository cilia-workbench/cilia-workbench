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
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Port;
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

			addRelationship(model, bundle);
			CiliaJarModel ipojo = bundle.getModel();

			if (ipojo != null) {
				for (MediatorComponent mc : ipojo.getMediatorComponents()) {
					addRelationship(bundle, mc);
					for (Port p : mc.getPorts())
						addRelationship(mc, p);
					
					for (Property p : mc.getProperties())
						addRelationship(mc, p);
				}

				for (Processor p : ipojo.getProcessors())
					addRelationship(bundle, p);

				for (Scheduler s : ipojo.getSchedulers())
					addRelationship(bundle, s);

				for (Dispatcher d : ipojo.getDispatchers())
					addRelationship(bundle, d);

				for (Collector c : ipojo.getCollectors())
					addRelationship(bundle, c);

				for (Sender s : ipojo.getSenders())
					addRelationship(bundle, s);

				for (Adapter a : ipojo.getAdapters())
					addRelationship(bundle, a);
			}
		}
	}
}
