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
package fr.liglab.adele.cilia.workbench.designer.view.specrepositoryview;

import fr.liglab.adele.cilia.workbench.designer.parser.spec.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.InPort;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.OutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider;

/**
 * LabelProvider for the Spec repository view.
 * 
 * @author Etienne Gandrille
 */
public class SpecLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider#getImageDescriptor(java.lang.Object)
	 */
	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (obj instanceof SpecFile) {
			SpecFile file = (SpecFile) obj;
			if (file.getModel() != null)
				imageName = ImageDescriptorEnum.FILE;
			else
				imageName = ImageDescriptorEnum.FILE_ERROR;
		} else if (obj instanceof MediatorSpec)
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (obj instanceof Scheduler)
			imageName = ImageDescriptorEnum.SCHEDULER;
		else if (obj instanceof Processor)
			imageName = ImageDescriptorEnum.PROCESSOR;
		else if (obj instanceof Dispatcher)
			imageName = ImageDescriptorEnum.DISPATCHER;
		else if (obj instanceof Property)
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (obj instanceof Parameter)
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (obj instanceof InPort)
			imageName = ImageDescriptorEnum.PORT_IN;
		else if (obj instanceof OutPort)
			imageName = ImageDescriptorEnum.PORT_OUT;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
