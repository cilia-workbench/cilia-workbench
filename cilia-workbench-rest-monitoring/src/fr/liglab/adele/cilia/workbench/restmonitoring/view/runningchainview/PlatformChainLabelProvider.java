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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.AbstractGraphLabelProvider;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChainLabelProvider extends AbstractGraphLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return PlatformRepoService.getInstance().getContentProvider();
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {

		// to be implemented...
		throw new RuntimeException("Unsupported type: " + obj.getClass());

		// return imageName;
	}

	@Override
	public String getText(Object element) {
		String defval = super.getText(element);

		if (isCompatible(element, EntityConnectionData.class)) {
			return defval;
		} else {
			return defval;
		}
	}
}
