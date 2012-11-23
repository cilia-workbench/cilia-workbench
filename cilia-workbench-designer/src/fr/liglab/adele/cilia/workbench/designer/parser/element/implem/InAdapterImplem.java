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
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.element.InAdapter;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class InAdapterImplem extends InAdapter {

	String collector;
	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_NAMESPACE = "namespace";

	public InAdapterImplem(Node node) throws CiliaException {
		super(computeID(node), XMLPortsUtil.getPorts(node));

		Node subNode = XMLHelpers.findChild(node, "collector");
		if (subNode != null)
			collector = XMLHelpers.findAttributeValueOrEmpty(subNode, "type");
	}

	private static NameNamespaceID computeID(Node node) {
		String name = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_NAME);
		String namespace = XMLHelpers.findAttributeValue(node, XML_ATTR_NAMESPACE, CiliaConstants.CILIA_DEFAULT_NAMESPACE);
		return new NameNamespaceID(name, namespace);
	}

	public String getCollectorID() {
		return collector;
	}

	protected void setCollectorID(String subElement) {
		collector = subElement;
	}

	public CollectorImplem getCollector() {
		String id = getCollectorID();
		return CiliaJarRepoService.getInstance().getCollector(id);
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
		CiliaFlag e3 = CiliaError.checkStringNotNullOrEmpty(this, collector, "collector");

		if (getInPorts().size() != 0) {
			e1 = new CiliaError("InAdapter has " + getInPorts().size() + " in ports", this);
		}

		if (getOutPorts().size() != 1) {
			e2 = new CiliaError("InAdapter must have 1 and only 1 out port, not " + getOutPorts().size(), this);
		}

		return CiliaFlag.generateTab(tab, e1, e2, e3);
	}
}
