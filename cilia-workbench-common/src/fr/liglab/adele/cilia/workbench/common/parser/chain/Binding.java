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
package fr.liglab.adele.cilia.workbench.common.parser.chain;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLStringUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class Binding implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	private final String from;
	private final String to;

	public Binding(String from, String to) throws CiliaException {
		this.from = from;
		this.to = to;
	}

	public String getSource() {
		return from;
	}

	public String getDestination() {
		return to;
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

	public abstract ComponentRef getSourceComponent();

	public abstract ComponentRef getDestinationComponent();

	@Override
	public String toString() {
		return from + " - " + to;
	}

	@Override
	public Object getId() {
		return from + " - " + to;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		return new ArrayList<Changeset>();
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		CiliaFlag e1 = CiliaError.checkNotNull(this, getSourceId(), "binding source");
		CiliaFlag e2 = CiliaError.checkNotNull(this, getDestinationId(), "binding destination");
		CiliaFlag e3 = CiliaError.checkStringNotNullOrEmpty(this, getSourcePort(), "binding source port");
		CiliaFlag e4 = CiliaError.checkStringNotNullOrEmpty(this, getDestinationPort(), "binding destination port");

		return CiliaFlag.generateTab(list, e1, e2, e3, e4);
	}
}
