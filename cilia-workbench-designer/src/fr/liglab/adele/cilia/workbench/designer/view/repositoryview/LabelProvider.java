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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * The base class for implementing label providers. Thanks to this class, images an be shared between labels providers.
 * 
 * @author Etienne Gandrille
 */
public abstract class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {

	/**
	 * Finds the ImageDescriptor enum which corresponds ton an object.
	 * 
	 * @param obj
	 *            source object.
	 * @return the Image descriptor
	 */
	protected abstract ImageDescriptorEnum getImageDescriptor(Object obj);

	/**
	 * Represents ans stores an image.
	 * 
	 * @author Etienne Gandrille
	 */
	public enum ImageDescriptorEnum {
		FILE("icons/16/file.png"), FILE_ERROR("icons/16/fileError.png"), CHAIN("icons/16/chain.png"), ADAPTER_IN(
				"icons/16/adapterIn.png"), ADAPTER_OUT("icons/16/adapterOut.png"), REPOSITORY("icons/16/repo.png"), MEDIATOR(
				"icons/16/mediator.png"), SCHEDULER("icons/16/scheduler.png"), PROCESSOR("icons/16/processor.png"), DISPATCHER(
				"icons/16/dispatcher.png"), COLLECTOR("icons/16/collector.png"), SENDER("icons/16/sender.png"), PORT_IN(
				"icons/16/portIn.png"), PORT_OUT("icons/16/portOut.png");

		/** Path to find the physical image in the bundle */
		private String path;

		/** Image object, for rendering */
		private Image image;

		/**
		 * Contructor.
		 * 
		 * @param path
		 *            the path
		 */
		private ImageDescriptorEnum(String path) {
			this.path = path;
			this.image = null;
		}

		/**
		 * Gets the Image object for rendering. Creates the Image object if needed.
		 * 
		 * @return The Image object.
		 */
		public Image getImage() {
			if (image == null)
				image = createImageFromPath(path);

			return image;
		}

		/**
		 * Creates an image object from a path in the bundle.
		 * 
		 * @param imagePath
		 * @return the Image object.
		 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		return getImageDescriptor(obj).getImage();
	}
}
