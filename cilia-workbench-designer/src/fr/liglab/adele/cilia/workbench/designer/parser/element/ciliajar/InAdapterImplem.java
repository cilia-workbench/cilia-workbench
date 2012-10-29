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

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * 
 * @author Etienne Gandrille
 */
public class InAdapterImplem extends AdapterImplem {

	String collector;

	public InAdapterImplem(Node node) throws CiliaException {
		AdapterImplemUtil.initAdapter(node, this, "collector");
	}

	@Override
	public AdapterType getType() {
		return AdapterType.IN;
	}

	public String getCollector() {
		return collector;
	}

	protected void setSubElement(String subElement) {
		collector = subElement;
	}

	@Override
	protected String getSubElement() {
		return collector;
	}
}
