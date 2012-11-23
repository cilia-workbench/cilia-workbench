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
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.InAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.OutAdapter;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * A few static methods for adapters implementations.
 * 
 * @author Etienne Gandrille
 */
public abstract class AdapterImplemUtil {

	public static final String XML_NODE_NAME = "adapter";
	public static final String XML_ATTR_PATTERN = "pattern";

	private static String IN_PATTERN = "in-only";
	private static String OUT_PATTERN = "out-only";

	/**
	 * Factory for creating an adapter.
	 * 
	 * @param node
	 * 
	 * @return {@link InAdapter}, an {@link OutAdapter} or <code>null</code> in
	 *         case of error.
	 */
	public static Adapter createAdapter(Node node) {
		Adapter retval = null;

		try {
			String pattern = XMLHelpers.findAttributeValueOrException(node, XML_ATTR_PATTERN);
			if (pattern.equalsIgnoreCase(IN_PATTERN))
				retval = new InAdapterImplem(node);
			else if (pattern.equalsIgnoreCase(OUT_PATTERN))
				retval = new OutAdapterImplem(node);
			else
				throw new CiliaException("Unknown pattern : addapter can't be created");
		} catch (CiliaException e) {
			// ERROR : pattern not found...
			e.printStackTrace();
		}

		return retval;
	}
}
