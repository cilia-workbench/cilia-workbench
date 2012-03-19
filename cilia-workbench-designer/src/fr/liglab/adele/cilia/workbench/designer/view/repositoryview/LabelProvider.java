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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {

	protected abstract String getImagePath(Object obj);

	private Map<String, Image> base = new HashMap<String, Image>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imagePath = getImagePath(obj);
		Image image = base.get(imagePath);

		if (image == null) {
			image = createImageFromPath(imagePath);
			base.put(imagePath, image);
		}

		return image;
	}

	private static Image createImageFromPath(String imagePath) {
		org.osgi.framework.Bundle bundle = Activator.getDefault().getBundle();
		URL url = FileLocator.find(bundle, new Path(imagePath), null);
		try {
			url = new URL("platform:/plugin/fr.liglab.adele.cilia.workbench.common/" + imagePath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

		return imageDesc.createImage();
	}
}
