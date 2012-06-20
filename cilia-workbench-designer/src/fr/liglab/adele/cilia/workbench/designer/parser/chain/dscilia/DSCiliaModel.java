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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.Mergeable;

/**
 * A {@link DSCiliaModel} represents the content of a <strong>well
 * formed<strong> {@link DSCiliaFile}.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaModel implements DisplayedInPropertiesView, Mergeable {

	public static final String ROOT_NODE_NAME = "cilia";

	private File file;

	private List<Chain> model = new ArrayList<Chain>();

	public DSCiliaModel(File file) throws Exception {
		this.file = file;

		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, Chain.XML_NODE_NAME))
			model.add(new Chain(node));
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	public File getFile() {
		return file;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		DSCiliaModel newInstance = (DSCiliaModel) other;

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public List<Chain> getChains() {
		return model;
	}
}
