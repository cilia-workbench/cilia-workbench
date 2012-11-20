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

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CollectorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem.RefMediatorSpec;
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

	protected ImageDescriptorEnum personalizeImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName = null;

		if (isCompatible(obj, CollectorImplem.class))
			imageName = ImageDescriptorEnum.COLLECTOR;
		else if (isCompatible(obj, SenderImplem.class))
			imageName = ImageDescriptorEnum.SENDER;
		else if (isCompatible(obj, RefMediatorSpec.class))
			imageName = ImageDescriptorEnum.SUPER_TYPE;

		return imageName;
	}
}
