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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.reflection.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class Property implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	public static final String XML_NODE_NAME = "property";

	public static final String XML_ATTR_KEY = "key";
	private String key;
	public static final String XML_ATTR_VALUE = "value";
	private String value;

	public Property(Node node) throws CiliaException {
		ReflectionUtil.setAttribute(node, XML_ATTR_KEY, this, "key");
		ReflectionUtil.setAttribute(node, XML_ATTR_VALUE, this, "value");
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Object getId() {
		return key;
	}

	@Override
	public String toString() {
		if (key == null || key.length() == 0)
			return "<undefined> = " + value;
		if (value == null || value.length() == 0)
			return key + " = <undefined>";

		return key + " = " + value;
	}

	@Override
	public List<Changeset> merge(Object newInstance) throws CiliaException {
		return MergeUtil.computeUpdateChangeset(newInstance, this, "value");
	}

	public static Node createXMLProperty(Document document, Node parent, String key, String value) {
		return XMLHelpers.createNode(document, parent, XML_NODE_NAME, XML_ATTR_KEY, key, XML_ATTR_VALUE, value);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, key, "key");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, value, "value");

		return CiliaFlag.generateTab(e1, e2);
	}
}
