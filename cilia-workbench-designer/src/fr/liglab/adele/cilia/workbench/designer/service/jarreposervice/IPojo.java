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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents a VALID metadata.
 * 
 * @see {@link Bundle}.
 * 
 * @author Etienne Gandrille
 */
public class IPojo {

	/** The mediator components. */
	private List<MediatorComponent> mediatorComponents = new ArrayList<MediatorComponent>();

	/** The processors. */
	private List<Processor> processors = new ArrayList<Processor>();

	/** The schedulers. */
	private List<Scheduler> schedulers = new ArrayList<Scheduler>();

	/** The dispatchers. */
	private List<Dispatcher> dispatchers = new ArrayList<Dispatcher>();

	/** The collectors. */
	private List<Collector> collectors = new ArrayList<Collector>();

	/** The senders. */
	private List<Sender> senders = new ArrayList<Sender>();

	/** The adapters. */
	private List<Adapter> adapters = new ArrayList<Adapter>();

	/**
	 * Instantiates a new metadata.xml abstraction, using reflection on the DOM
	 * model.
	 * 
	 * @param node
	 *            the XML DOM node
	 * @throws Exception
	 *             error while parsing the metadata.xml file.
	 */
	public IPojo(Node node) throws Exception {

		NodeList childs = node.getChildNodes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE) {

					String nodeName = child.getNodeName().toLowerCase();

					if (nodeName.equals("processor"))
						processors.add(new Processor(child));
					else if (nodeName.equals("scheduler"))
						schedulers.add(new Scheduler(child));
					else if (nodeName.equals("dispatcher"))
						dispatchers.add(new Dispatcher(child));
					else if (nodeName.equals("collector"))
						collectors.add(new Collector(child));
					else if (nodeName.equals("sender"))
						senders.add(new Sender(child));
					else if (nodeName.equals("adapter"))
						adapters.add(new Adapter(child));
					else if (nodeName.equals("mediator-component"))
						mediatorComponents.add(new MediatorComponent(child));
				}
			}
		}
	}

	/**
	 * Gets the mediator components.
	 * 
	 * @return the mediator components
	 */
	public List<MediatorComponent> getMediatorComponents() {
		return mediatorComponents;
	}

	/**
	 * Gets the processors.
	 * 
	 * @return the processors
	 */
	public List<Processor> getProcessors() {
		return processors;
	}

	/**
	 * Gets the schedulers.
	 * 
	 * @return the schedulers
	 */
	public List<Scheduler> getSchedulers() {
		return schedulers;
	}

	/**
	 * Gets the dispatchers.
	 * 
	 * @return the dispatchers
	 */
	public List<Dispatcher> getDispatchers() {
		return dispatchers;
	}

	/**
	 * Gets the collectors.
	 * 
	 * @return the collectors
	 */
	public List<Collector> getCollectors() {
		return collectors;
	}

	/**
	 * Gets the senders.
	 * 
	 * @return the senders
	 */
	public List<Sender> getSenders() {
		return senders;
	}

	/**
	 * Gets the adapters.
	 * 
	 * @return the adapters
	 */
	public List<Adapter> getAdapters() {
		return adapters;
	}
}
