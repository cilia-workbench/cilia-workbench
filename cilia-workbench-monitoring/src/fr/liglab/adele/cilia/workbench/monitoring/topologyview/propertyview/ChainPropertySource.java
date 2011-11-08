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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import fr.liglab.adele.cilia.ChainReadOnly;

/**
 * Bridge the gap between the Chain model object (of type ChainReadOnly) and the
 * properties view.
 */
public class ChainPropertySource extends CommonPropertySource<ChainReadOnly> implements IPropertySource {

	/** Properties id. */
	private final String PROPERTY_ID = PROPERTY_PREFIX + "id";

	public ChainPropertySource(ChainReadOnly modelObject) {
		super(modelObject);
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor id = createBasicPropertyDescriptor(PROPERTY_ID, "chainID");
		return new IPropertyDescriptor[]{id};
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getBasicPropertyValue(Object id) {
		if (id.equals(PROPERTY_ID)) {
			return modelObject.getId();
		}
		
		return null;
	}
}
