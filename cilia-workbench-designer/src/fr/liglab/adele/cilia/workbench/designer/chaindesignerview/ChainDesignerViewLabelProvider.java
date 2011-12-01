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
package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.providers.GenericLabelProvider;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.MediatorInstance;

/**
 * Label provider used in the chain designer view.
 * 
 * @author Etienne Gandrille
 */
public class ChainDesignerViewLabelProvider extends GenericLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object obj) {
		if (obj instanceof AdapterInstance)
			return ((AdapterInstance) obj).getId();
		if (obj instanceof MediatorInstance)
			return ((MediatorInstance) obj).getId();

		if (obj instanceof EntityConnectionData)
			return "";

		throw new RuntimeException("Unsupported type: " + obj.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public String getImageKey(Object obj) {
		if (obj instanceof AdapterInstance)
			return Activator.IN_ADAPTER;
		else if (obj instanceof MediatorInstance)
			return Activator.MEDIATOR;
		else if (obj instanceof EntityConnectionData)
			return null;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());
	}
}
