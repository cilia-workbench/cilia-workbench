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
package fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar;

import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class ComponentPartImplemHelper {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_NAMESPACE = "namespace";
	public static final String XML_ATTR_CLASSNAME = "classname";

	private ComponentPartImplemHelper() {
	}

	static List<ParameterImplem> init(Node node, Object object) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_NAME, object, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, object, "namespace", CiliaConstants.CILIA_DEFAULT_NAMESPACE);
		ReflectionUtil.setAttribute(node, XML_ATTR_CLASSNAME, object, "classname");
		return ParameterImplem.findParameters(node);
	}
}
