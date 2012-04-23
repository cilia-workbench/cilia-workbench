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

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.InPort;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.OutPort;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Sender;
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
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider#getImageDescriptor(java.lang.Object)
	 */
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;
		if (obj instanceof Adapter) {
			Adapter adapt = (Adapter) obj;
			if (adapt.getPattern().equals("in-only"))
				imageName = ImageDescriptorEnum.ADAPTER_IN;
			else
				imageName = ImageDescriptorEnum.ADAPTER_OUT;
		} else if (obj instanceof CiliaJarFile)
			imageName = ImageDescriptorEnum.REPOSITORY;
		else if (obj instanceof Collector)
			imageName = ImageDescriptorEnum.COLLECTOR;
		else if (obj instanceof Dispatcher)
			imageName = ImageDescriptorEnum.DISPATCHER;
		else if (obj instanceof MediatorComponent)
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (obj instanceof Processor)
			imageName = ImageDescriptorEnum.PROCESSOR;
		else if (obj instanceof Scheduler)
			imageName = ImageDescriptorEnum.SCHEDULER;
		else if (obj instanceof Sender)
			imageName = ImageDescriptorEnum.SENDER;
		else if (obj instanceof Property)
			imageName = ImageDescriptorEnum.PROPERTY;
		else if (obj instanceof InPort)
			imageName = ImageDescriptorEnum.PORT_IN;
		else if (obj instanceof OutPort)
			imageName = ImageDescriptorEnum.PORT_OUT;
		else if (obj instanceof Parameter)
			imageName = ImageDescriptorEnum.PROPERTY;
		
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
