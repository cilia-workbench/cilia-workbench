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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * The Class MediatorInstance.
 * 
 * @author Etienne Gandrille
 */
public class MediatorInstance extends ComponentInstance {

	/**
	 * Instantiates a new mediator instance, using reflection on the DOM node.
	 * 
	 * @param node
	 *            the DOMnode
	 * @throws MetadataException
	 *             XML parsing error, or reflexion error.
	 */
	public MediatorInstance(Node node) throws MetadataException {
		super(node);
	}

	/**
	 * Merge another {@link MediatorInstance} into the current one. Differences
	 * between the argument and the current object are injected into the current
	 * object.
	 * 
	 * @param newInstance
	 *            an 'up-to-date' object
	 * @return a list of {@link Changeset}, which can be empty.
	 */
	protected Changeset[] merge(MediatorInstance newInstance) {
		if (type.equals(newInstance.type))
			return new Changeset[0];
		else {
			type = newInstance.type;
			Changeset[] retval = new Changeset[1];
			retval[0] = new Changeset(Operation.UPDATE, this);
			return retval;
		}
	}
}
