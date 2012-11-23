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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * Represents an adapter content. Because in and out adapters have different
 * internal structures, this class is intented to be subclassed.
 * 
 * @author Etienne Gandrille
 */
public abstract class AdapterPartImplem implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_CLASSNAME = "classname";

	private final String name;
	private final String classname;
	private final ParameterListImplem parameters;

	public AdapterPartImplem(Node node) throws CiliaException {
		parameters = new ParameterListImplem(node);
		name = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_NAME);
		classname = XMLHelpers.findAttributeValueOrEmpty(node, XML_ATTR_CLASSNAME);
	}

	public List<ParameterImplem> getParameters() {
		List<ParameterImplem> retval = new ArrayList<ParameterImplem>();
		for (ParameterDefinition p : parameters.getParameters())
			retval.add((ParameterImplem) p);
		return retval;
	}

	@Override
	public Object getId() {
		return name;
	}

	@Override
	public String toString() {
		return Strings.nullToEmpty(name);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> flagsTab = IdentifiableUtils.getErrorsNonUniqueId(this, parameters.getParameters());

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, classname, "class name");

		return CiliaFlag.generateTab(flagsTab, e1, e2);
	}
}
