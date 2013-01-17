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
package fr.liglab.adele.cilia.workbench.designer.parser.element.implem;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractModel;
import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;

/**
 * Represents the content of a <strong>well formed<strong> {@link CiliaJarFile}.
 * 
 * @author Etienne Gandrille
 */
public class CiliaJarModel extends AbstractModel {

	public static final String[] ROOT_NODE_NAME = { "ipojo", "cilia" };

	private List<MediatorImplem> mediatorComponents = new ArrayList<MediatorImplem>();
	private List<ProcessorImplem> processors = new ArrayList<ProcessorImplem>();
	private List<SchedulerImplem> schedulers = new ArrayList<SchedulerImplem>();
	private List<DispatcherImplem> dispatchers = new ArrayList<DispatcherImplem>();
	private List<CollectorImplem> collectors = new ArrayList<CollectorImplem>();
	private List<SenderImplem> senders = new ArrayList<SenderImplem>();
	private List<Adapter> adapters = new ArrayList<Adapter>();

	public CiliaJarModel(PhysicalResource file) throws CiliaException {
		super(file, ROOT_NODE_NAME);

		Node root = getRootNode(getDocument());

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
					else if (nodeName.equals(CollectorImplem.XML_NODE_NAME))
						collectors.add(new CollectorImplem(child));
					else if (nodeName.equals(SenderImplem.XML_NODE_NAME))
						senders.add(new SenderImplem(child));
					else if (nodeName.equals(AdapterImplemUtil.XML_NODE_NAME)) {
						Adapter a = AdapterImplemUtil.createAdapter(child);
						if (a != null)
							adapters.add(a);
					} else if (nodeName.equals(InOutAdapterImplem.XML_NODE_NAME)) {
						adapters.add(new InOutAdapterImplem(child));
					} else if (nodeName.equals(MediatorImplem.XML_NODE_NAME))
						mediatorComponents.add(new MediatorImplem(child));
				}
			}
		}
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

	public List<CollectorImplem> getCollectors() {
		return collectors;
	}

	public List<SenderImplem> getSenders() {
		return senders;
	}

	public List<Adapter> getAdapters() {
		return adapters;
	}

	public List<Adapter> getInAdapters() {
		return getAdapters(AdapterType.IN);
	}

	public List<Adapter> getOutAdapters() {
		return getAdapters(AdapterType.OUT);
	}

	public List<Adapter> getInOutAdapters() {
		return getAdapters(AdapterType.INOUT);
	}

	private List<Adapter> getAdapters(AdapterType type) {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (Adapter adapter : adapters)
			if (adapter.getType().equals(type))
				retval.add(adapter);

		return retval;
	}
}
