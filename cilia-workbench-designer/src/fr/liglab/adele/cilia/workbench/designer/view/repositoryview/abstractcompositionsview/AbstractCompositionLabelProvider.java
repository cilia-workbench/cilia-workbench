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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.abstractcompositionsview;

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.AdapterInstanceRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorInstanceRef;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;

/**
 * LabelProvider for the {@link AbstractCompositionsRepoService}.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionLabelProvider extends CiliaLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return AbstractCompositionsRepoService.getInstance().getContentProvider();
	}

	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (isCompatible(obj, AbstractCompositionFile.class))
			imageName = ImageDescriptorEnum.FILE;
		else if (isCompatible(obj, AbstractChain.class))
			imageName = ImageDescriptorEnum.CHAIN;
		else if (isCompatible(obj, AdapterInstanceRef.class))
			imageName = ImageDescriptorEnum.ADAPTER_IN;
		else if (isCompatible(obj, MediatorInstanceRef.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, Binding.class))
			imageName = ImageDescriptorEnum.BINDING;

		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
