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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.common.XMLReflectionUtil;

public class SpecModel {

	String filePath;
	
	List<MediatorSpec> mediatorSpecs = new ArrayList<MediatorSpec>();
	
	public SpecModel(String filePath) throws MetadataException {
		this.filePath = filePath;
		
		Document document = XMLHelpers.getDocument(filePath);
		Node root = getSpecNode(document);

		for (Node node : XMLReflectionUtil.findChildren(root, "mediator-specification"))
			mediatorSpecs.add(new MediatorSpec(node));
	}
	
	private static Node getSpecNode(Document document) throws MetadataException {
		return XMLHelpers.getRootNode(document, "cilia-specifications");
	}
}
