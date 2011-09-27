/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Adapter;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Bundle;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Collector;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Processor;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Sender;

/**
 * The Class MetadataLabelProvider.
 */
public class MetadataLabelProvider extends LabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imageName;
		if (obj instanceof Adapter) {
			Adapter adapt = (Adapter) obj;
			if (adapt.getPattern().equals("in-only"))
				imageName = "icons/16/inAdapter.png";
			else
				imageName = "icons/16/outAdapter.png";
		}
		else if (obj instanceof Bundle)
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
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		org.osgi.framework.Bundle bundle = Activator.getDefault().getBundle();
		URL url = FileLocator.find(bundle, new Path(imageName), null);
		try {
			url = new URL("platform:/plugin/fr.liglab.adele.cilia.workbench.common/" + imageName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

		return imageDesc.createImage();
	}
}
