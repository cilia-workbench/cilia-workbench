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
package fr.liglab.adele.cilia.workbench.designer.parser.element.spec;

import java.util.ArrayList;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.element.Parameter;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterList;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class ParameterListSpec extends ParameterList {

	public ParameterListSpec(Node node) throws CiliaException {
		parameters = new ArrayList<Parameter>();
		Node rootParam = XMLHelpers.findChild(node, "parameters");
		if (rootParam != null) {
			Node[] params = XMLHelpers.findChildren(rootParam, "parameter");
			for (Node param : params)
				parameters.add(new ParameterSpec(param));
		}
	}
}
