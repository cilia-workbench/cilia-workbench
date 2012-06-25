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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public class AbstractBinding extends Binding {

	public static final String XML_FROM_CARD_ATTR = "from-cardinality";
	public static final String XML_TO_CARD_ATTR = "to-cardinality";

	private Cardinality fromCardinality;
	private Cardinality toCardinality;

	public AbstractBinding(Node node, NameNamespaceID chainId) throws CiliaException {
		super(node, chainId);

		String fc = XMLHelpers.findAttributeValue(node, XML_FROM_CARD_ATTR);
		fromCardinality = Cardinality.getCardinality(fc);
		String tc = XMLHelpers.findAttributeValue(node, XML_TO_CARD_ATTR);
		toCardinality = Cardinality.getCardinality(tc);
	}

	protected AbstractChain getChain() {
		return AbstractCompositionsRepoService.getInstance().findChain(chainId);
	}

	public Cardinality getSourceCardinality() {
		return fromCardinality;
	}

	public Cardinality getDestinationCardinality() {
		return toCardinality;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);

		AbstractBinding newInstance = (AbstractBinding) other;
		if (!fromCardinality.equals(newInstance.getSourceCardinality())
				|| !toCardinality.equals(newInstance.getDestinationCardinality())) {
			fromCardinality = newInstance.getSourceCardinality();
			toCardinality = newInstance.getDestinationCardinality();
			retval.add(new Changeset(Operation.UPDATE, this));
		}

		return retval;
	}
}
