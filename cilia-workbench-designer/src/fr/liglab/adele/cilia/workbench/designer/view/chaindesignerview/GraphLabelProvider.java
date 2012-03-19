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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.osgi.framework.Bundle;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;

/**
 * Label provider for the tree viewer.
 * 
 * @author Etienne Gandrille
 */
public class GraphLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object obj) {
		if (obj instanceof AdapterInstance)
			return ((AdapterInstance) obj).getId();
		if (obj instanceof MediatorInstance)
			return ((MediatorInstance) obj).getId();

		if (obj instanceof EntityConnectionData)
			return "";

		throw new RuntimeException("Unsupported type: " + obj.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imageName;
		if (obj instanceof AdapterInstance)
			imageName = "icons/16/adapterIn.png";
		else if (obj instanceof MediatorInstance)
			imageName = "icons/16/mediator.png";
		else if (obj instanceof EntityConnectionData)
			imageName = null;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		if (imageName != null) {
			Bundle bundle = Activator.getDefault().getBundle();
			URL url = FileLocator.find(bundle, new Path(imageName), null);
			try {
				url = new URL("platform:/plugin/fr.liglab.adele.cilia.workbench.common/" + imageName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

			return imageDesc.createImage();
		} else
			return null;
	}
}
