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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.service.common.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.chainview.common.GraphDrawable;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainElement extends NameNamespace implements DisplayedInPropertiesView, ErrorsAndWarningsFinder,
		Mergeable, GraphDrawable {

	public static final String XML_NODE_NAME = "chain";

	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public static final String XML_ROOT_MEDIATORS_NAME = "mediators";
	public static final String XML_ROOT_ADAPTERS_NAME = "adapters";
	public static final String XML_ROOT_BINDINGS_NAME = "bindings";

	public ChainElement(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_ID, this, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, this, "namespace");
	}

	@Override
	public String toString() {
		return getName();
	}
}
