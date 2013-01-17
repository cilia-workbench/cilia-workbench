/**
 * Copyright 2012-2013 France Télécom 
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
package fr.liglab.adele.cilia.workbench.common.misc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * 
 * @author Etienne Gandrille
 */
public class ImageBuilder {

	private static ImageBuilder INSTANCE = new ImageBuilder();

	Map<String, Image> map = new HashMap<String, Image>();

	private ImageBuilder() {
	}

	public static ImageBuilder getINSTANCE() {
		return INSTANCE;
	}

	public Image getImage(String path) {
		if (!map.containsKey(path))
			map.put(path, createImage(path));
		return map.get(path);
	}

	private static Image createImage(String imagePath) {
		Bundle bundle = Activator.getInstance().getBundle();
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
