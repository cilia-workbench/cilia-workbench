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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.marker.MarkerFinder;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * The Class Property.
 * @author Etienne Gandrille
 */
public class Property implements DisplayedInPropertiesView, MarkerFinder {
	
	/** The key. */
	private final String key;
	
	/** The value. */
	private final String value;

	/**
	 * Instantiates a new property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return key + " = " + value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		if (!(arg0 instanceof Property))
			return false;

		Property prop = (Property) arg0;

		if (prop.getKey() != key || prop.getValue() != value)
			return false;

		return true;
	}

	@Override
	public void createMarkers(Object rootSourceProvider) {
		// name validation
		if (key == null || key.length() == 0)
			CiliaMarkerUtil.createErrorMarker("key can't be null or empty", JarRepoService.getInstance(), this);
		if (value == null || value.isEmpty())
			CiliaMarkerUtil.createWarningMarker("Property " + key + " does't have its value defined", SpecRepoService.getInstance(), this);
	}
}
