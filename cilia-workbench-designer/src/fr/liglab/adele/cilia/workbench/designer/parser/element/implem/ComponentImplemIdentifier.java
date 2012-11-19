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
package fr.liglab.adele.cilia.workbench.designer.parser.element.implem;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class ComponentImplemIdentifier implements ErrorsAndWarningsFinder, Identifiable {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_NAMESPACE = "namespace";
	public static final String XML_ATTR_CLASSNAME = "classname";

	protected NameNamespaceID id = new NameNamespaceID();
	private String classname;

	public ComponentImplemIdentifier(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_NAME, id, "name");
		ReflectionUtil.setAttribute(node, XML_ATTR_NAMESPACE, id, "namespace", CiliaConstants.CILIA_DEFAULT_NAMESPACE);
		ReflectionUtil.setAttribute(node, XML_ATTR_CLASSNAME, this, "classname");
	}

	@Override
	public NameNamespaceID getId() {
		return id;
	}

	@Override
	public String toString() {
		return id.getName();
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, id.getName(), "name");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, id.getNamespace(), "namespace");
		CiliaFlag e3 = CiliaError.checkStringNotNullOrEmpty(this, classname, "class name");

		return CiliaFlag.generateTab(e1, e2, e3);
	}
}