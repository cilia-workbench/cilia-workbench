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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.common.Mergeable;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainModel<ChainType extends ChainElement<?>> implements DisplayedInPropertiesView, Mergeable {

	protected File file;

	protected List<ChainType> model = new ArrayList<ChainType>();

	private final String rootNodeName;

	public ChainModel(File file, String rootNodeName) {
		this.file = file;
		this.rootNodeName = rootNodeName;
	}

	public File getFile() {
		return file;
	}

	protected Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, rootNodeName);
	}

	public List<ChainType> getChains() {
		return model;
	}

	public String getRootNodeName() {
		return rootNodeName;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		@SuppressWarnings("unchecked")
		ChainModel<ChainType> newInstance = (ChainModel<ChainType>) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getChains(), model));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public void createChain(NameNamespaceID id) throws CiliaException {

		// Document creation
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);
		Element child = document.createElement(ChainElement.XML_NODE_NAME);
		child.setAttribute(ChainElement.XML_ATTR_ID, id.getName());
		child.setAttribute(ChainElement.XML_ATTR_NAMESPACE, id.getNamespace());
		root.appendChild(child);

		// Write it back to file system
		XMLHelpers.writeDOM(document, file);

		// Notifies Repository
		getRepository().updateModel();
	}

	protected abstract ChainRepoService<?, ?, ?> getRepository();
}
