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
package fr.liglab.adele.cilia.workbench.common.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * 
 * 
 * @author Etienne Gandrille
 */
public abstract class GenericLabelProvider extends LabelProvider {

	protected abstract String getImageKey(Object obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imageKey = getImageKey(obj);

		if (imageKey != null) {
			Activator plugin = fr.liglab.adele.cilia.workbench.common.Activator.getDefault();
			ImageRegistry imageRegistry = plugin.getImageRegistry();
			return imageRegistry.get(imageKey);
		} else {
			return null;
		}
	}
}
