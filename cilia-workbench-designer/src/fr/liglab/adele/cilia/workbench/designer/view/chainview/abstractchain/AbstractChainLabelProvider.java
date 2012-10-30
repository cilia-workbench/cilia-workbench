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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.AbstractGraphLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractBinding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.AdapterRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ComponentRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractChainLabelProvider extends AbstractGraphLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return AbstractCompositionsRepoService.getInstance().getContentProvider();
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {

		ImageDescriptorEnum imageName;
		if (isCompatible(obj, AdapterRef.class)) {
			AdapterRef adapter = (AdapterRef) obj;
			if (adapter.getReferencedObject() != null) {
				if (adapter.getReferencedObject().getType() == AdapterType.IN)
					imageName = ImageDescriptorEnum.ADAPTER_IN;
				else
					imageName = ImageDescriptorEnum.ADAPTER_OUT;
			} else
				imageName = ImageDescriptorEnum.ADAPTER_IN;
		} else if (isCompatible(obj, MediatorRef.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, EntityConnectionData.class))
			imageName = ImageDescriptorEnum.ONLY_TEXT;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}

	@Override
	public String getText(Object element) {
		String defval = super.getText(element);

		if (isCompatible(element, EntityConnectionData.class)) {
			EntityConnectionData ecd = (EntityConnectionData) element;
			ComponentRef src = (ComponentRef) ecd.source;
			ComponentRef dst = (ComponentRef) ecd.dest;
			// Be careful during refactoring with AbstractBinding !
			AbstractBinding binding = (AbstractBinding) src.getOutgoingBinding(dst);

			if (binding == null)
				return "";
			else {
				String from = binding.getSourceCardinality().stringId();
				String to = binding.getDestinationCardinality().stringId();
				return from + " --> " + to;
			}
		} else
			return defval;
	}
}
