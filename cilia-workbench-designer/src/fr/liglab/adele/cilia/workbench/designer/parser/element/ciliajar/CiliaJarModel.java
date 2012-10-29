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
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.AdapterImplem;

/**
 * Represents the content of a <strong>well formed<strong> {@link CiliaJarFile}.
 * 
 * @author Etienne Gandrille
 */
public class CiliaJarModel implements DisplayedInPropertiesView {

	public static final String ROOT_NODE_NAME = "ipojo";

	private List<MediatorImplem> mediatorComponents = new ArrayList<MediatorImplem>();
	private List<ProcessorImplem> processors = new ArrayList<ProcessorImplem>();
	private List<SchedulerImplem> schedulers = new ArrayList<SchedulerImplem>();
	private List<DispatcherImplem> dispatchers = new ArrayList<DispatcherImplem>();
	private List<Collector> collectors = new ArrayList<Collector>();
	private List<Sender> senders = new ArrayList<Sender>();
	private List<AdapterImplem> adapters = new ArrayList<AdapterImplem>();

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

					if (nodeName.equals(ProcessorImplem.XML_NODE_NAME))
						processors.add(new ProcessorImplem(child));
					else if (nodeName.equals(SchedulerImplem.XML_NODE_NAME))
						schedulers.add(new SchedulerImplem(child));
					else if (nodeName.equals(DispatcherImplem.XML_NODE_NAME))
						dispatchers.add(new DispatcherImplem(child));
					else if (nodeName.equals(Collector.XML_NODE_NAME))
						collectors.add(new Collector(child));
					else if (nodeName.equals(Sender.XML_NODE_NAME))
						senders.add(new Sender(child));
					else if (nodeName.equals(AdapterUtil.XML_NODE_NAME)) {
						AdapterImplem a = AdapterUtil.createAdapter(child);
						if (a != null)
							adapters.add(a);
					} else if (nodeName.equals(MediatorImplem.XML_NODE_NAME))
						mediatorComponents.add(new MediatorImplem(child));
				}
			}
		}
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, ROOT_NODE_NAME);
	}

	public List<MediatorImplem> getMediatorComponents() {
		return mediatorComponents;
	}

	public List<ProcessorImplem> getProcessors() {
		return processors;
	}

	public List<SchedulerImplem> getSchedulers() {
		return schedulers;
	}

	public List<DispatcherImplem> getDispatchers() {
		return dispatchers;
	}

	public List<Collector> getCollectors() {
		return collectors;
	}

	public List<Sender> getSenders() {
		return senders;
	}

	public List<AdapterImplem> getAdapters() {
		return adapters;
	}
}
