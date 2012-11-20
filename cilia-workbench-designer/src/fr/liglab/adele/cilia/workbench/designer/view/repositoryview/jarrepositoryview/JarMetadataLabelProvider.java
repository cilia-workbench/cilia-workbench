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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.jarrepositoryview;

import fr.liglab.adele.cilia.workbench.common.parser.element.InAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.InPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutPort;
import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CollectorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.DispatcherImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem.RefMediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.ParameterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.ProcessorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.PropertyImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SchedulerImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SenderImplem;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class JarMetadataLabelProvider extends CiliaLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return JarRepoService.getInstance().getContentProvider();
	}

	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;
		if (isCompatible(obj, InAdapter.class))
			imageName = ImageDescriptorEnum.ADAPTER_IN;
		else if (isCompatible(obj, OutAdapter.class))
			imageName = ImageDescriptorEnum.ADAPTER_OUT;
		else if (isCompatible(obj, CiliaJarFile.class))
			imageName = ImageDescriptorEnum.REPOSITORY;
		else if (isCompatible(obj, CollectorImplem.class))
			imageName = ImageDescriptorEnum.COLLECTOR;
		else if (isCompatible(obj, DispatcherImplem.class))
			imageName = ImageDescriptorEnum.DISPATCHER;
		else if (isCompatible(obj, MediatorImplem.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, ProcessorImplem.class))
			imageName = ImageDescriptorEnum.PROCESSOR;
		else if (isCompatible(obj, SchedulerImplem.class))
			imageName = ImageDescriptorEnum.SCHEDULER;
		else if (isCompatible(obj, SenderImplem.class))
			imageName = ImageDescriptorEnum.SENDER;
		else if (isCompatible(obj, PropertyImplem.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, InPort.class))
			imageName = ImageDescriptorEnum.PORT_IN;
		else if (isCompatible(obj, OutPort.class))
			imageName = ImageDescriptorEnum.PORT_OUT;
		else if (isCompatible(obj, ParameterImplem.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, RefMediatorSpec.class))
			imageName = ImageDescriptorEnum.SUPER_TYPE;

		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
