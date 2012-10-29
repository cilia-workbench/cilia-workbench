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
package fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;

/**
 * Represents the content of a <strong>well formed<strong> {@link SpecFile}.
 * 
 * @author Etienne Gandrille
 */
public class SpecModel implements DisplayedInPropertiesView, Mergeable {

	public static final String XML_NODE_NAME = "cilia-specifications";

	private File file;
	private List<MediatorSpec> mediatorSpecs = new ArrayList<MediatorSpec>();

	public SpecModel(File file) throws CiliaException {
		this.file = file;

		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, MediatorSpec.XML_NODE_NAME))
			mediatorSpecs.add(new MediatorSpec(node));
	}

	public List<MediatorSpec> getMediatorSpecs() {
		return mediatorSpecs;
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, XML_NODE_NAME);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		SpecModel newInstance = (SpecModel) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getMediatorSpecs(), mediatorSpecs));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public void deleteMediatorSpec(NameNamespaceID id) throws CiliaException {

		// Finding target node
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLMediatorNode(document, id);

		if (target != null) {
			getRootNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, file);

			// Notifies Repository
			SpecRepoService.getInstance().updateModel();
		}
	}

	public void updateMediatorSpec(NameNamespaceID id, List<String> inPorts, List<String> outPorts,
			List<String> mediatorProperties, List<String> schedulerParam, List<String> processorParam,
			List<String> dispatcherParam) throws CiliaException {

		// Finding target node
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLMediatorNode(document, id);
		Node parent = getRootNode(document);

		if (target != null)
			parent.removeChild(target);

		// id and namespace attributes
		Element spec = MediatorSpec.createXMLSpec(document, parent, id);

		// ports
		for (String inPort : inPorts)
			MediatorSpec.createXMLInPort(document, spec, inPort);
		for (String outPort : outPorts)
			MediatorSpec.createXMLOutPort(document, spec, outPort);

		// mediatorProperties
		for (String key : mediatorProperties)
			MediatorSpec.createMediatorProperty(document, spec, key);

		// scheduler params
		for (String param : schedulerParam)
			MediatorSpec.createSchedulerParameter(document, spec, param);

		// processor params
		for (String param : processorParam)
			MediatorSpec.createProcessorParameter(document, spec, param);

		// dispatcher params
		for (String param : dispatcherParam)
			MediatorSpec.createDispatcherParameter(document, spec, param);

		XMLHelpers.writeDOM(document, file);

		// Notifies Repository
		SpecRepoService.getInstance().updateModel();
	}

	private Node findXMLMediatorNode(Document document, NameNamespaceID id) throws CiliaException {
		Node root = getRootNode(document);
		Node[] results = XMLHelpers.findChildren(root, MediatorSpec.XML_NODE_NAME, MediatorSpec.XML_ATTR_ID,
				id.getName(), MediatorSpec.XML_ATTR_NAMESPACE, id.getNamespace());

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	public void createMediatorSpec(NameNamespaceID id) throws CiliaException {

		if (SpecRepoService.getInstance().isNewMediatorSpecAllowed(id) == null) {
			Document document = XMLHelpers.getDocument(file);
			Node parent = getRootNode(document);

			MediatorSpec.createXMLSpec(document, parent, id);

			// Write it back to file system
			XMLHelpers.writeDOM(document, file);

			// Notifies Repository
			SpecRepoService.getInstance().updateModel();
		}
	}
}
