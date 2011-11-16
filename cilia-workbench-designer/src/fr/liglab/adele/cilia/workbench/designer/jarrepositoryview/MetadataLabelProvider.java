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

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Bundle;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.InPort;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.OutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Sender;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.LabelProvider;

/**
 * The Class MetadataLabelProvider.
 */
public class MetadataLabelProvider extends LabelProvider {

	protected String getImagePath(Object obj) {
		String imageName;
		if (obj instanceof Adapter) {
			Adapter adapt = (Adapter) obj;
			if (adapt.getPattern().equals("in-only"))
				imageName = "icons/16/adapterIn.png";
			else
				imageName = "icons/16/adapterOut.png";
		} else if (obj instanceof Bundle)
			imageName = "icons/16/repo.png";
		else if (obj instanceof Collector)
			imageName = "icons/16/collector.png";
		else if (obj instanceof Dispatcher)
			imageName = "icons/16/dispatcher.png";
		else if (obj instanceof MediatorComponent)
			imageName = "icons/16/mediator.png";
		else if (obj instanceof Processor)
			imageName = "icons/16/processor.png";
		else if (obj instanceof Scheduler)
			imageName = "icons/16/scheduler.png";
		else if (obj instanceof Sender)
			imageName = "icons/16/sender.png";
		else if (obj instanceof InPort)
			imageName = "icons/16/portIn.png";
		else if (obj instanceof OutPort)
			imageName = "icons/16/portOut.png";
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
