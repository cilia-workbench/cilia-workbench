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
package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder {

	private String from;
	private String to;

	public Binding(Node node) throws MetadataException {
		XMLReflectionUtil.setAttribute(node, "from", this, "from");
		XMLReflectionUtil.setAttribute(node, "to", this, "to");
	}

	public String getSourceId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(from);
	}

	public String getDestinationId() {
		return XMLStringUtil.getBeforeSeparatorOrAll(to);
	}

	public Changeset[] merge(Binding newInstance) {
		return new Changeset[0];
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
}
