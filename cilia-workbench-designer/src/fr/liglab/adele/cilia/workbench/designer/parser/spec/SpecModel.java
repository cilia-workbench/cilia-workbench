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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * Represents the content of a <strong>well formed<strong> {@link SpecFile}.
 * 
 * @author Etienne Gandrille
 */
public class SpecModel implements DisplayedInPropertiesView {

	/** XML Root node name */
	public static final String XML_NODE_NAME = "cilia-specifications";

	/** Physical file path. */
	private String filePath;

	/** Specs owned by the file. */
	private List<MediatorSpec> mediatorSpecs = new ArrayList<MediatorSpec>();

	/**
	 * Instantiates a new spec model.
	 * 
	 * @param filePath
	 *            the file path
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public SpecModel(String filePath) throws MetadataException {
		this.filePath = filePath;

		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		for (Node node : XMLHelpers.findChildren(root, MediatorSpec.XML_NODE_NAME))
			mediatorSpecs.add(new MediatorSpec(node));
	}

	/**
	 * Gets the mediator specs.
	 * 
	 * @return the mediator specs
	 */
	public List<MediatorSpec> getMediatorSpecs() {
		return mediatorSpecs;
	}

	/**
	 * Gets the root node.
	 * 
	 * @param document
	 *            the document
	 * @return the root node
	 * @throws MetadataException
	 *             the metadata exception
	 */
	private static Node getRootNode(Document document) throws MetadataException {
		return XMLHelpers.getRootNode(document, XML_NODE_NAME);
	}

	/**
	 * Merge.
	 * 
	 * @param newInstance
	 *            the new instance
	 * @return the changeset[]
	 */
	public Changeset[] merge(SpecModel newInstance) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<MediatorSpec> itr = mediatorSpecs.iterator(); itr.hasNext();) {
			MediatorSpec old = itr.next();
			NameNamespace id = (NameNamespace) old.getId();

			MediatorSpec updated = PullElementUtil.pullMediatorSpec(newInstance, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (MediatorSpec c : newInstance.getMediatorSpecs()) {
			mediatorSpecs.add(c);
			retval.add(new Changeset(Operation.ADD, c));
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}

	public void deleteMediatorSpec(NameNamespace id) throws MetadataException {

		// Finding target node
		File file = new File(filePath);
		Document document = XMLHelpers.getDocument(file);
		Node target = findXMLMediatorNode(document, id);

		if (target != null) {
			getRootNode(document).removeChild(target);
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			SpecRepoService.getInstance().updateModel();
		}
	}

	public void updateMediatorSpec(NameNamespace id, List<String> inPorts, List<String> outPorts,
			Map<String, String> mediatorProperties, List<String> schedulerParam, List<String> processorParam,
			List<String> dispatcherParam) throws MetadataException {

		// Finding target node
		File file = new File(filePath);
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
		for (String key : mediatorProperties.keySet())
			MediatorSpec.createMediatorProperty(document, spec, key, mediatorProperties.get(key));

		// scheduler params
		for (String param : schedulerParam)
			MediatorSpec.createSchedulerParameter(document, spec, param);

		// processor params
		for (String param : processorParam)
			MediatorSpec.createProcessorParameter(document, spec, param);

		// dispatcher params
		for (String param : dispatcherParam)
			MediatorSpec.createDispatcherParameter(document, spec, param);

		XMLHelpers.writeDOM(document, filePath);

		// Notifies Repository
		SpecRepoService.getInstance().updateModel();
	}

	private Node findXMLMediatorNode(Document document, NameNamespace id) throws MetadataException {
		Node root = getRootNode(document);
		Node[] results = XMLHelpers.findChildren(root, MediatorSpec.XML_NODE_NAME, MediatorSpec.XML_ATTR_ID,
				id.getName(), MediatorSpec.XML_ATTR_NAMESPACE, id.getNamespace());

		if (results.length == 0)
			return null;
		else
			return results[0];
	}

	public void createMediatorSpec(NameNamespace id) throws MetadataException {

		if (SpecRepoService.getInstance().isNewMediatorSpecAllowed(id) == null) {
			File file = new File(filePath);
			Document document = XMLHelpers.getDocument(file);
			Node parent = getRootNode(document);

			MediatorSpec.createXMLSpec(document, parent, id);

			// Write it back to file system
			XMLHelpers.writeDOM(document, filePath);

			// Notifies Repository
			SpecRepoService.getInstance().updateModel();
		}
	}
}
