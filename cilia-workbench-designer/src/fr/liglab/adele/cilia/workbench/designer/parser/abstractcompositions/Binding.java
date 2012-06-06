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
package fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_NODE_NAME = "binding";

	private String from;
	private String to;
	private Cardinality fromCardinality;
	private Cardinality toCardinality;

	public Binding(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, "from", this, "from");
		ReflectionUtil.setAttribute(node, "to", this, "to");
		String fc = XMLHelpers.findAttributeValue(node, "from-cardinality");
		fromCardinality = Cardinality.getCardinality(fc);
		String tc = XMLHelpers.findAttributeValue(node, "to-cardinality");
		toCardinality = Cardinality.getCardinality(tc);
	}

	public String getSourceId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(from);
	}

	public String getDestinationId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(to);
	}

	public String getSourcePort() {
		return XMLStringUtil.getAfterSeparatorOrNothing(from);
	}

	public String getDestinationPort() {
		return XMLStringUtil.getAfterSeparatorOrNothing(to);
	}

	public Cardinality getFromCardinality() {
		return fromCardinality;
	}

	public Cardinality getToCardinality() {
		return toCardinality;
	}

	public String getSource() {
		return from;
	}

	public String getDestination() {
		return to;
	}

	@Override
	public String toString() {
		return from + " - " + to;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, from, "from");
		CiliaFlag e2 = CiliaError.checkStringNotNullOrEmpty(this, to, "to");

		return CiliaFlag.generateTab(e1, e2);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		return new ArrayList<Changeset>();
	}

	@Override
	public Object getId() {
		return from + " - " + to;
	}
}
