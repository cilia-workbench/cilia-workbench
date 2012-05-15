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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider;

/**
 * Label provider for the tree viewer.
 * 
 * @author Etienne Gandrille
 */
public class GraphLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider
	 * #getContentProvider()
	 */
	@Override
	protected GenericContentProvider getContentProvider() {
		return null;
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {

		ImageDescriptorEnum imageName;
		if (isCompatible(obj, AdapterInstance.class))
			imageName = ImageDescriptorEnum.ADAPTER_IN;
		else if (isCompatible(obj, MediatorInstance.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, EntityConnectionData.class))
			imageName = ImageDescriptorEnum.NOTHING;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
