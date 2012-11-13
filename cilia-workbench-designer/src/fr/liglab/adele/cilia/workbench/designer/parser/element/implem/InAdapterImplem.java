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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class InAdapterImplem extends AdapterImplem {

	String collector;

	public InAdapterImplem(Node node) throws CiliaException {
		super(node);
		AdapterImplemUtil.initAdapter(node, this, "collector");
	}

	@Override
	public AdapterType getType() {
		return AdapterType.IN;
	}

	public String getCollectorID() {
		return collector;
	}

	public CollectorImplem getCollector() {
		String id = getCollectorID();
		return JarRepoService.getInstance().getCollector(id);
	}

	protected void setSubElement(String subElement) {
		collector = subElement;
	}

	@Override
	protected String getSubElement() {
		return collector;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] flagsTab = super.getErrorsAndWarnings();
		CiliaError e1 = null;
		CiliaError e2 = null;

		if (getInPorts().size() != 0) {
			e1 = new CiliaError("InAdapter has " + getInPorts().size() + " in ports", this);
		}

		if (getOutPorts().size() != 1) {
			e2 = new CiliaError("InAdapter must have 1 and only 1 out port, not " + getOutPorts().size(), this);
		}

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}
