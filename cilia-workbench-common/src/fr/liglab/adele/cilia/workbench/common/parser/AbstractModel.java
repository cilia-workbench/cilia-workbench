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
package fr.liglab.adele.cilia.workbench.common.parser;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractModel implements DisplayedInPropertiesView {

	private PhysicalResource resource;

	private final String[] allowedRootNodeName;

	public AbstractModel(PhysicalResource resource, String... rootNodeName) {
		this.resource = resource;
		this.allowedRootNodeName = rootNodeName;
	}

	protected Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, allowedRootNodeName);
	}

	protected Document getDocument() throws CiliaException {
		InputStream stream = resource.getContentAsStream();
		Document retval = XMLHelpers.getDocument(stream);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}

	protected void writeToFile(Document document) throws CiliaException {
		XMLHelpers.writeDOM(document, resource.getJavaFile());
	}
}
