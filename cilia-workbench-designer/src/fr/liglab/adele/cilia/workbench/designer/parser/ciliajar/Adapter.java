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

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaConstants;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.NameNamespace;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Adapter extends NameNamespace implements DisplayedInPropertiesView {

	String elementType;

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
			String pattern = XMLHelpers.findAttributeValue(node, "pattern");
			if (pattern.equalsIgnoreCase(IN_PATTERN))
				retval = new InAdapter(node);
			else if (pattern.equalsIgnoreCase(OUT_PATTERN))
				retval = new OutAdapter(node);
			else
				throw new CiliaException("Unknown pattern : addapter can't be created");
		} catch (CiliaException e) {
			// ERROR : pattern not found...
			e.printStackTrace();
		}

		return retval;
	}

	protected Adapter(Node node, String subNodeName) throws CiliaException {
		ReflectionUtil.setAttribute(node, "name", this, "name");
		ReflectionUtil.setAttribute(node, "namespace", this, "namespace", CiliaConstants.getDefaultNamespace());

		try {
			Node subNode = XMLHelpers.findChild(node, subNodeName);
			if (subNode != null)
				ReflectionUtil.setAttribute(subNode, "type", this, "elementType");
		} catch (Exception e) {
			// Error reported by getErrorsAndWarnings
		}
	}

	public abstract boolean isInAdapter();

	public abstract boolean isOutAdapter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder
	 * #createErrorsAndWarnings(java.lang.Object)
	 */
	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] flagsTab = super.getErrorsAndWarnings();

		CiliaError e1 = CiliaError.checkStringNotNullOrEmpty(this, elementType, "elementType");

		return CiliaFlag.generateTab(flagsTab, e1);
	}
}
