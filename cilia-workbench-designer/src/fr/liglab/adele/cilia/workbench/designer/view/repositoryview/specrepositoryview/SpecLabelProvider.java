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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.specrepositoryview;

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.GenericInPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.GenericOutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.NameProperty;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * LabelProvider for the Spec repository view.
 * 
 * @author Etienne Gandrille
 */
public class SpecLabelProvider extends CiliaLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return SpecRepoService.getInstance().getContentProvider();
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (isCompatible(obj, SpecFile.class))
			imageName = ImageDescriptorEnum.FILE;
		else if (isCompatible(obj, MediatorSpec.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, Scheduler.class))
			imageName = ImageDescriptorEnum.SCHEDULER;
		else if (isCompatible(obj, Processor.class))
			imageName = ImageDescriptorEnum.PROCESSOR;
		else if (isCompatible(obj, Dispatcher.class))
			imageName = ImageDescriptorEnum.DISPATCHER;
		else if (isCompatible(obj, NameProperty.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, Parameter.class))
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (isCompatible(obj, GenericInPort.class))
			imageName = ImageDescriptorEnum.PORT_IN;
		else if (isCompatible(obj, GenericOutPort.class))
			imageName = ImageDescriptorEnum.PORT_OUT;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
