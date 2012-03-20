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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public class Property {

	private String key;
	private String value;
	private Node node;

	public Property(Node node) throws MetadataException {
		this.node = node;
		XMLReflectionUtil.setRequiredAttribute(node, "key", this, "key");
		XMLReflectionUtil.setRequiredAttribute(node, "value", this, "value");
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}

	public Changeset[] merge(Property newInstance) {
		if (newInstance.getValue().equals(value))
			return new Changeset[0];
		else {
			value = newInstance.getValue();
			Changeset c = new Changeset(Operation.UPDATE, this);
			Changeset[] retval = new Changeset[1];
			retval[0] = c;
			return retval;
		}
	}
}
