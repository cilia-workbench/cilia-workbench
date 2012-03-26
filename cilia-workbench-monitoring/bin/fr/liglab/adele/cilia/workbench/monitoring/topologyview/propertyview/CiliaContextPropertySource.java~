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

import fr.liglab.adele.cilia.CiliaContextReadOnly;

/**
 * The Class CiliaContextPropertySource.
 */
public class CiliaContextPropertySource extends CommonPropertySource<CiliaContextReadOnly> implements IPropertySource {

	/** Properties id. */
	protected final String PROPERTY_VERSION = PROPERTY_PREFIX + "version";

	/**
	 * Constructor.
	 * 
	 * @param adaptableObject
	 *            the adaptable object
	 */
	public CiliaContextPropertySource(CiliaContextReadOnly adaptableObject) {
		super(adaptableObject);
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor version = createBasicPropertyDescriptor(PROPERTY_VERSION, "cilia version");
		return new IPropertyDescriptor[]{version};
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getBasicPropertyValue(Object id) {

		if (id.equals(PROPERTY_VERSION))
			return modelObject.getCiliaVersion();

		// Not found
		return null;
	}
}
