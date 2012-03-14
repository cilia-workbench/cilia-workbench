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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class ComponentPart {

	List<Parameter> parameters = new ArrayList<Parameter>();
	
	public ComponentPart(Node node) throws MetadataException {
		
		Node rootParam = XMLReflectionUtil.findChild(node, "parameters");
		if (rootParam != null) {
			Node[] params = XMLReflectionUtil.findChildren(rootParam, "parameter");
			for (Node param : params)
				parameters.add(new Parameter(param));
		}
	}
}
