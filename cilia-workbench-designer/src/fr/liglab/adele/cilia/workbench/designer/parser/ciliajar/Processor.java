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
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class Processor extends Element {

	private String methodName;
	private String methodDataType;

	public Processor(Node node) throws MetadataException {
		super(node);
		
		Node methodNode = XMLHelpers.findChild(node, "method");
		if (methodNode == null)
			throw new MetadataException("method element not found");
		XMLReflectionUtil.setAttribute(methodNode, "name", this, "methodName");
		XMLReflectionUtil.setAttribute(methodNode, "data.type", this, "methodDataType");
	}
	
	@Override
	public void createMarkers(Object rootSourceProvider) {
		super.createMarkers(rootSourceProvider);
		if (methodName == null)
			CiliaMarkerUtil.createErrorMarker("methodName can't be null", JarRepoService.getInstance(), this);
		if (methodDataType == null)
			CiliaMarkerUtil.createErrorMarker("methodDataType can't be null", JarRepoService.getInstance(), this);
	}
}
