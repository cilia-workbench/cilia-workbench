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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.marker.MarkerFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Parameter implements DisplayedInPropertiesView, MarkerFinder {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_VALUE = "value";
	public static final String XML_ATTR_FIELD = "field";
	
	private String name;
	private String method;
	private String value;
	private String field;
	
	public Parameter(Node node) throws MetadataException {
		XMLReflectionUtil.setAttribute(node, XML_ATTR_NAME, this, "name");
		XMLReflectionUtil.setAttribute(node, XML_ATTR_METHOD, this, "method");
		XMLReflectionUtil.setAttribute(node, XML_ATTR_VALUE, this, "value");
		XMLReflectionUtil.setAttribute(node, XML_ATTR_FIELD, this, "field");
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String retval = name;
		
		if (value != null)
			retval = retval + " = " + value;
		if (method != null)
			retval = retval + " [" + method + "]";
		if (field != null)
			retval = retval + " [" + field + "]";
		
		return retval;
	}

	@Override
	public void createMarkers(Object rootSourceProvider) {
		if (name == null)
			CiliaMarkerUtil.createErrorMarker("name can't be null", JarRepoService.getInstance(), this);
		if (method != null && field != null)
			CiliaMarkerUtil.createErrorMarker("method and field parameters are excusive", JarRepoService.getInstance(), this);
	}
}
