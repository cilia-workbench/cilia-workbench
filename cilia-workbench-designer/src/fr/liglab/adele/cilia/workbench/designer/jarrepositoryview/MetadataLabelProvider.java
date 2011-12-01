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
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.providers.GenericLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Adapter;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Bundle;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Collector;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.InPort;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.OutPort;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Processor;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.Sender;

/**
 * The Class MetadataLabelProvider.
 */
public class MetadataLabelProvider extends GenericLabelProvider {

	protected String getImageKey(Object obj) {
		String imageName;
		if (obj instanceof Adapter) {
			Adapter adapt = (Adapter) obj;
			if (adapt.getPattern().equals("in-only"))
				imageName = Activator.IN_ADAPTER;
			else
				imageName = Activator.OUT_ADAPTER;
		} else if (obj instanceof Bundle)
			imageName = Activator.REPO;
		else if (obj instanceof Collector)
			imageName = Activator.COLLECTOR;
		else if (obj instanceof Dispatcher)
			imageName = Activator.DISPATCHER;
		else if (obj instanceof MediatorComponent)
			imageName = Activator.MEDIATOR;
		else if (obj instanceof Processor)
			imageName = Activator.PROCESSOR;
		else if (obj instanceof Scheduler)
			imageName = Activator.SCHEDULER;
		else if (obj instanceof Sender)
			imageName = Activator.SENDER;
		else if (obj instanceof InPort)
			imageName = Activator.IN_PORT;
		else if (obj instanceof OutPort)
			imageName = Activator.OUT_PORT;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
