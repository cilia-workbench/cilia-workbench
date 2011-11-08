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

import fr.liglab.adele.cilia.MediatorReadOnly;

/**
 * The Class ComponentPropertySource.
 */
public class ComponentPropertySource extends CommonPropertySource<MediatorReadOnly> implements IPropertySource {

	/* Properties id. */
	private final String PROPERTY_ID = PROPERTY_PREFIX + "id";
	private final String PROPERTY_NAMESPACE = PROPERTY_PREFIX + "namespace";
	private final String PROPERTY_CATEGORY = PROPERTY_PREFIX + "category";
	

	/**
	 * Instantiates a new component property source.
	 *
	 * @param modelObject the model object
	 */
	public ComponentPropertySource(MediatorReadOnly modelObject) {
		super(modelObject);
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor id = createBasicPropertyDescriptor(PROPERTY_ID, "id");
		IPropertyDescriptor ns = createBasicPropertyDescriptor(PROPERTY_NAMESPACE, "namespace");
		IPropertyDescriptor cat = createBasicPropertyDescriptor(PROPERTY_CATEGORY, "category");
		return new IPropertyDescriptor[]{id, ns, cat};
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getBasicPropertyValue(Object id) {
		if (id.equals(PROPERTY_ID))
			return modelObject.getId();
		if (id.equals(PROPERTY_NAMESPACE))
			return modelObject.getNamespace();
		if (id.equals(PROPERTY_CATEGORY))
			return modelObject.getCategory();
				
		// Not found
		return null;
	}
}
