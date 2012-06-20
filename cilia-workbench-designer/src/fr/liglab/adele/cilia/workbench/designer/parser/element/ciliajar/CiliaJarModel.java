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
package fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.GenericAdapter;

/**
 * Represents the content of a <strong>well formed<strong> {@link CiliaJarFile}.
 * 
 * @author Etienne Gandrille
 */
public class CiliaJarModel implements DisplayedInPropertiesView {

	public static final String ROOT_NODE_NAME = "ipojo";

	private List<MediatorComponent> mediatorComponents = new ArrayList<MediatorComponent>();
	private List<Processor> processors = new ArrayList<Processor>();
	private List<Scheduler> schedulers = new ArrayList<Scheduler>();
	private List<Dispatcher> dispatchers = new ArrayList<Dispatcher>();
	private List<Collector> collectors = new ArrayList<Collector>();
	private List<Sender> senders = new ArrayList<Sender>();
	private List<GenericAdapter> adapters = new ArrayList<GenericAdapter>();

	public CiliaJarModel(File file) throws CiliaException {

		InputStream is = XMLHelpers.inputStreamFromFileInJarArchive(file, "metadata.xml");
		Document document = XMLHelpers.getDocument(is);
		Node root = getRootNode(document);

		NodeList childs = root.getChildNodes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE) {

					String nodeName = child.getNodeName().toLowerCase();

					if (nodeName.equals(Processor.XML_NODE_NAME))
						processors.add(new Processor(child));
					else if (nodeName.equals(Scheduler.XML_NODE_NAME))
						schedulers.add(new Scheduler(child));
					else if (nodeName.equals(Dispatcher.XML_NODE_NAME))
						dispatchers.add(new Dispatcher(child));
					else if (nodeName.equals(Collector.XML_NODE_NAME))
						collectors.add(new Collector(child));
					else if (nodeName.equals(Sender.XML_NODE_NAME))
						senders.add(new Sender(child));
					else if (nodeName.equals(AdapterUtil.XML_NODE_NAME)) {
						GenericAdapter a = AdapterUtil.createAdapter(child);
						if (a != null)
							adapters.add(a);
					} else if (nodeName.equals(MediatorComponent.XML_NODE_NAME))
						mediatorComponents.add(new MediatorComponent(child));
				}
			}
		}
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	public List<MediatorComponent> getMediatorComponents() {
		return mediatorComponents;
	}

	public List<Processor> getProcessors() {
		return processors;
	}

	public List<Scheduler> getSchedulers() {
		return schedulers;
	}

	public List<Dispatcher> getDispatchers() {
		return dispatchers;
	}

	public List<Collector> getCollectors() {
		return collectors;
	}

	public List<Sender> getSenders() {
		return senders;
	}

	public List<GenericAdapter> getAdapters() {
		return adapters;
	}
}
