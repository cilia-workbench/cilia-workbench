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

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.XMLReflectionUtil;

/**
 * The Class Binding.
 * 
 * @author Etienne Gandrille
 */
public class Binding {

	/** The from. */
	private String from;

	/** The to. */
	private String to;

	/**
	 * Instantiates a new binding, using reflection on the DOM node.
	 * 
	 * @param node
	 *            the DOM node
	 * @throws MetadataException
	 *             XML parsing error, or reflexion error.
	 */
	public Binding(Node node) throws MetadataException {
		XMLReflectionUtil.setRequiredAttribute(node, "from", this, "from");
		XMLReflectionUtil.setRequiredAttribute(node, "to", this, "to");
	}

	/**
	 * Gets the binding source.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return from;
	}

	/**
	 * Gets the binding destination.
	 * 
	 * @return the destination
	 */
	public String getDestination() {
		return to;
	}

	/**
	 * Gets the source id.
	 * 
	 * @return the source id
	 */
	public String getSourceId() {
		return StringUtil.getBeforeSeparatorOrAll(from);
	}

	/**
	 * Gets the destination id.
	 * 
	 * @return the destination id
	 */
	public String getDestinationId() {
		return StringUtil.getBeforeSeparatorOrAll(to);
	}

	/**
	 * Gets the source port.
	 * 
	 * @return the source port
	 */
	public String getSourcePort() {
		return StringUtil.getAfterSeparatorOrNothing(from);
	}

	/**
	 * Gets the destination port.
	 * 
	 * @return the destination port
	 */
	public String getDestinationPort() {
		return StringUtil.getAfterSeparatorOrNothing(to);
	}

	/**
	 * Merge another {@link Binding} into the current one. Differences between
	 * the argument and the current object are injected into the current object.
	 * 
	 * @param newInstance
	 *            an 'up-to-date' object
	 * @return a list of {@link Changeset}, which can be empty.
	 */
	protected Changeset[] merge(Binding newInstance) {
		return new Changeset[0];
	}
}
