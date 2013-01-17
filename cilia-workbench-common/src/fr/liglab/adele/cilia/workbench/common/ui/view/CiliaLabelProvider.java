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
package fr.liglab.adele.cilia.workbench.common.ui.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.parser.chain.AdapterRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;
import fr.liglab.adele.cilia.workbench.common.parser.element.Dispatcher;
import fr.liglab.adele.cilia.workbench.common.parser.element.InAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.InOutAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.InPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.Mediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.parser.element.Processor;
import fr.liglab.adele.cilia.workbench.common.parser.element.Property;
import fr.liglab.adele.cilia.workbench.common.parser.element.Scheduler;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider.FakeElement;
import fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview.SourceProviderField;

/**
 * The base class for implementing label providers. Thanks to this class, images
 * an be shared between labels providers.
 * 
 * @author Etienne Gandrille
 */
public abstract class CiliaLabelProvider extends LabelProvider {

	/**
	 * This method is provided to be subclassed, for giving icons for unknown
	 * objects.
	 * 
	 * @param obj
	 * @return
	 */
	protected ImageDescriptorEnum personalizeImageDescriptor(Object obj) {
		return null;
	}

	protected ImageDescriptorEnum defaultPersonalizeImageDescriptor(Object obj) {
		// common rules
		if (isCompatible(obj, AbstractFile.class))
			return ImageDescriptorEnum.FILE;

		if (obj instanceof Chain)
			return ImageDescriptorEnum.CHAIN;

		// Mediator
		if (isCompatible(obj, MediatorRef.class))
			return ImageDescriptorEnum.MEDIATOR;
		if (isCompatible(obj, Mediator.class))
			return ImageDescriptorEnum.MEDIATOR;

		// Adapter
		if (isCompatible(obj, InAdapter.class))
			return ImageDescriptorEnum.ADAPTER_IN;
		if (isCompatible(obj, OutAdapter.class))
			return ImageDescriptorEnum.ADAPTER_OUT;
		if (isCompatible(obj, InOutAdapter.class))
			return ImageDescriptorEnum.ADAPTER_IN_OUT;
		if (isCompatible(obj, AdapterRef.class)) {
			AdapterRef adapter = (AdapterRef) obj;
			if (adapter.getReferencedComponentDefinition() != null) {
				if (adapter.getReferencedComponentDefinition().getType() == AdapterType.IN)
					return ImageDescriptorEnum.ADAPTER_IN;
				else if (adapter.getReferencedComponentDefinition().getType() == AdapterType.OUT)
					return ImageDescriptorEnum.ADAPTER_OUT;
				else
					return ImageDescriptorEnum.ADAPTER_IN_OUT;
			} else
				return ImageDescriptorEnum.ADAPTER_IN;
		}

		// Component part
		if (isCompatible(obj, Scheduler.class))
			return ImageDescriptorEnum.SCHEDULER;
		if (isCompatible(obj, Processor.class))
			return ImageDescriptorEnum.PROCESSOR;
		if (isCompatible(obj, Dispatcher.class))
			return ImageDescriptorEnum.DISPATCHER;

		// Ports
		if (isCompatible(obj, InPort.class))
			return ImageDescriptorEnum.PORT_IN;
		if (isCompatible(obj, OutPort.class))
			return ImageDescriptorEnum.PORT_OUT;

		// Binding
		if (isCompatible(obj, Binding.class))
			return ImageDescriptorEnum.BINDING;
		if (isCompatible(obj, EntityConnectionData.class))
			return ImageDescriptorEnum.ONLY_TEXT;

		// Property and parameters
		if (isCompatible(obj, Property.class))
			return ImageDescriptorEnum.PROPERTY;
		if (isCompatible(obj, ParameterDefinition.class))
			return ImageDescriptorEnum.PROPERTY;

		return null;
	}

	private ImageDescriptorEnum getImageDescriptor(Object obj) {

		if (personalizeImageDescriptor(obj) != null)
			return personalizeImageDescriptor(obj);

		if (defaultPersonalizeImageDescriptor(obj) != null)
			return defaultPersonalizeImageDescriptor(obj);

		throw new RuntimeException("Unsupported type: " + obj.getClass());
	}

	protected abstract GenericContentProvider getContentProvider();

	/**
	 * Represents and stores an image.
	 * 
	 * @author Etienne Gandrille
	 */
	public enum ImageDescriptorEnum {
		NOTHING(null), ONLY_TEXT(null), FILE("file.png"), CHAIN("chain.png"), ADAPTER_IN("adapterIn.png"), ADAPTER_OUT("adapterOut.png"), ADAPTER_IN_OUT(
				"adapterInOut.png"), REPOSITORY("repo.png"), MEDIATOR("mediator.png"), SCHEDULER("scheduler.png"), PROCESSOR("processor.png"), DISPATCHER(
				"dispatcher.png"), COLLECTOR("collector.png"), SENDER("sender.png"), PORT_IN("portIn.png"), PORT_OUT("portOut.png"), PROPERTY("property.png"), BINDING(
				"binding.png"), SUPER_TYPE("super-type.png");

		private String fileName;
		private Image imageOK;
		private Image imageError;
		private Image imageWarning;

		private ImageDescriptorEnum(String fileName) {
			this.fileName = fileName;
			this.imageOK = null;
			this.imageError = null;
			this.imageWarning = null;
		}

		public Image getImageOK() {
			if (fileName == null)
				return null;
			if (imageOK == null)
				imageOK = createImageFromPath("icons/16/" + fileName);

			return imageOK;
		}

		public Image getImageError() {
			if (fileName == null)
				return null;
			if (imageError == null)
				imageError = createImageFromPath("icons/16-error/" + fileName);

			return imageError;
		}

		public Image getImageWarning() {
			if (fileName == null)
				return null;
			if (imageWarning == null)
				imageWarning = createImageFromPath("icons/16-warning/" + fileName);

			return imageWarning;
		}

		private static Image createImageFromPath(String imagePath) {
			org.osgi.framework.Bundle bundle = Activator.getInstance().getBundle();
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

	public Image getImage(Object obj) {

		ImageDescriptorEnum descriptor = getImageDescriptor(obj);

		switch (getImageModifier(obj)) {
		case IMarker.SEVERITY_INFO:
			return descriptor.getImageOK();
		case IMarker.SEVERITY_WARNING:
			return descriptor.getImageWarning();
		case IMarker.SEVERITY_ERROR:
			return descriptor.getImageError();
		}

		// impossible ?!
		return null;
	}

	private int getImageModifier(Object obj) {

		List<Object> list = new ArrayList<Object>();
		list.add(obj);

		if (getContentProvider() != null) {
			for (Object o : getContentProvider().getAllSubItems(obj))
				list.add(o);
		}

		try {

			boolean warning = false;

			for (IMarker marker : CiliaMarkerUtil.findMarkers()) {
				Object object = marker.getAttribute(SourceProviderField.FIELD_ID);
				if (list.contains(object)) {
					int severity = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);

					if (severity == IMarker.SEVERITY_ERROR)
						return IMarker.SEVERITY_ERROR;
					else if (severity == IMarker.SEVERITY_WARNING)
						warning = true;
				}
			}

			if (warning)
				return IMarker.SEVERITY_WARNING;
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return IMarker.SEVERITY_INFO;
	}

	@Override
	public String getText(Object element) {
		String def = super.getText(element);
		if (getImageDescriptor(element).equals(ImageDescriptorEnum.NOTHING))
			return "";
		else
			return def;
	}

	protected static boolean isCompatible(Object obj, Class<?> theClass) {

		if (obj instanceof FakeElement) {
			FakeElement fe = (FakeElement) obj;
			if (theClass.isAssignableFrom(fe.getFakeClass()))
				return true;
			return false;
		}

		return theClass.isAssignableFrom(obj.getClass());
	}
}
