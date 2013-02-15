/**
 * Copyright 2012-2013 France Télécom 
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
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.element.InOutAdapter;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class InOutAdapterImplem extends InOutAdapter {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_NAMESPACE = "namespace";
	public static Object XML_NODE_NAME = "io-adapter";

	public InOutAdapterImplem(Node node, String physicalResourcePath) throws CiliaException {
		super(computeID(node), XMLPortsUtil.getPorts(node), physicalResourcePath);
	}

	private static NameNamespaceID computeID(Node node) {
		String name = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_NAME);
		String namespace = XMLHelpers.findAttributeValue(node, XML_ATTR_NAMESPACE, CiliaConstants.CILIA_DEFAULT_NAMESPACE);
		return new NameNamespaceID(name, namespace);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.IMPLEM;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();

		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getInPorts().size() == 0) {
			e1 = new CiliaError("InOutAdapter must have at least one in port", this);
		}

		if (getOutPorts().size() == 0) {
			e2 = new CiliaError("InOutAdapter must have at least one out port", this);
		}

		return CiliaFlag.generateTab(tab, e1, e2);
	}
}
