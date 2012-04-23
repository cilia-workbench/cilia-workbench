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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.SourceProviderField;

/**
 * The base class for implementing label providers. Thanks to this class, images
 * an be shared between labels providers.
 * 
 * @author Etienne Gandrille
 */
public abstract class LabelProvider extends
		org.eclipse.jface.viewers.LabelProvider {

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
		FILE("file.png"), CHAIN("chain.png"), ADAPTER_IN("adapterIn.png"), ADAPTER_OUT(
				"adapterOut.png"), REPOSITORY("repo.png"), MEDIATOR(
				"mediator.png"), SCHEDULER("scheduler.png"), PROCESSOR(
				"processor.png"), DISPATCHER("dispatcher.png"), COLLECTOR(
				"collector.png"), SENDER("sender.png"), PORT_IN("portIn.png"), PORT_OUT(
				"portOut.png"), PROPERTY("property.png"), BINDING("binding.png");

		/** Path to find the physical image in the bundle */
		private String fileName;

		/** Image object, for rendering */
		private Image imageOK;
		private Image imageError;
		private Image imageWarning;

		/**
		 * Contructor.
		 * 
		 * @param path
		 *            the path
		 */
		private ImageDescriptorEnum(String fileName) {
			this.fileName = fileName;
			this.imageOK = null;
			this.imageError = null;
			this.imageWarning = null;
		}

		/**
		 * Gets the Image object for rendering. Creates the Image object if
		 * needed.
		 * 
		 * @return The Image object.
		 */
		public Image getImageOK() {
			if (imageOK == null)
				imageOK = createImageFromPath("icons/16/" + fileName);

			return imageOK;
		}

		/**
		 * Gets the Image object for rendering. Creates the Image object if
		 * needed.
		 * 
		 * @return The Image object.
		 */
		public Image getImageError() {
			if (imageError == null)
				imageError = createImageFromPath("icons/16-error/" + fileName);

			return imageError;
		}

		/**
		 * Gets the Image object for rendering. Creates the Image object if
		 * needed.
		 * 
		 * @return The Image object.
		 */
		public Image getImageWarning() {
			if (imageWarning == null)
				imageWarning = createImageFromPath("icons/16-warning/"
						+ fileName);

			return imageWarning;
		}

		/**
		 * Creates an image object from a path in the bundle.
		 * 
		 * @param imagePath
		 * @return the Image object.
		 */
		private static Image createImageFromPath(String imagePath) {
			org.osgi.framework.Bundle bundle = Activator.getDefault()
					.getBundle();
			URL url = FileLocator.find(bundle, new Path(imagePath), null);
			try {
				url = new URL(
						"platform:/plugin/fr.liglab.adele.cilia.workbench.common/"
								+ imagePath);
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

		ImageDescriptorEnum descriptor = getImageDescriptor(obj);
		boolean warning = false;

		try {
			for (IMarker marker : CiliaMarkerUtil.findMarkers()) {
				if (marker.getAttribute(SourceProviderField.FIELD_ID) == obj) {
					int severity = marker.getAttribute(IMarker.SEVERITY,
							IMarker.SEVERITY_INFO);

					if (severity == IMarker.SEVERITY_ERROR)
						return descriptor.getImageError();
					else if (severity == IMarker.SEVERITY_WARNING)
						warning = true;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		if (warning == true)
			return descriptor.getImageWarning();

		return descriptor.getImageOK();
	}
}
