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
package fr.liglab.adele.cilia.workbench.designer.view.jarrepositoryview;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Sender;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.SuperMediator;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericInPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericOutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider;

/**
 * LabelProvider for the JAR repository view.
 * 
 * @author Etienne Gandrille
 */
public class JarMetadataLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider
	 * #getContentProvider()
	 */
	@Override
	protected GenericContentProvider getContentProvider() {
		return JarRepoService.getInstance().getContentProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider
	 * #getImageDescriptor(java.lang.Object)
	 */
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;
		if (obj instanceof GenericAdapter) {
			GenericAdapter adapter = (GenericAdapter) obj;
			if (adapter.getType() == AdapterType.IN)
				imageName = ImageDescriptorEnum.ADAPTER_IN;
			else
				imageName = ImageDescriptorEnum.ADAPTER_OUT;
		} else if (isCompatible(obj, CiliaJarFile.class))
			imageName = ImageDescriptorEnum.REPOSITORY;
		else if (isCompatible(obj, Collector.class))
			imageName = ImageDescriptorEnum.COLLECTOR;
		else if (isCompatible(obj, Dispatcher.class))
			imageName = ImageDescriptorEnum.DISPATCHER;
		else if (isCompatible(obj, MediatorComponent.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, Processor.class))
			imageName = ImageDescriptorEnum.PROCESSOR;
		else if (isCompatible(obj, Scheduler.class))
			imageName = ImageDescriptorEnum.SCHEDULER;
		else if (isCompatible(obj, Sender.class))
			imageName = ImageDescriptorEnum.SENDER;
		else if (isCompatible(obj, Property.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, GenericInPort.class))
			imageName = ImageDescriptorEnum.PORT_IN;
		else if (isCompatible(obj, GenericOutPort.class))
			imageName = ImageDescriptorEnum.PORT_OUT;
		else if (isCompatible(obj, Parameter.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, SuperMediator.class))
			imageName = ImageDescriptorEnum.SUPER_TYPE;

		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
