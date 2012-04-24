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

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;

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
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] e = super.getErrorsAndWarnings();
		CiliaFlag e1 = CiliaError.checkNotNull(this, methodName, "method name");
		CiliaFlag e2 = CiliaError.checkNotNull(this, methodDataType, "method data type");

		return CiliaFlag.generateTab(e, e1, e2);
	}
}
