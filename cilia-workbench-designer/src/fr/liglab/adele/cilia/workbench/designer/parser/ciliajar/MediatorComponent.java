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
package fr.liglab.adele.cilia.workbench.designer.parser.ciliajar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaMarkerUtil;
import fr.liglab.adele.cilia.workbench.common.marker.MarkerFinder;
import fr.liglab.adele.cilia.workbench.common.xml.MetadataException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.common.xml.XMLReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorComponent implements DisplayedInPropertiesView, MarkerFinder {

	private String name;

	private String schedulerName;
	private String processorName;
	private String dispatcherName;
	private List<Port> ports = new ArrayList<Port>();
	private List<Property> properties = new ArrayList<Property>();

	public MediatorComponent(Node node) throws MetadataException {

		XMLReflectionUtil.setAttribute(node, "name", this, "name");

		Map<String, String> attrMap = XMLHelpers.findAttributesValues(node);
		for (String attr : attrMap.keySet()) {
			if (!attr.equalsIgnoreCase("name"))
				properties.add(new Property(attr, attrMap.get(attr)));
		}

		Node schedulerNode = XMLHelpers.findChild(node, "scheduler");
		if (schedulerNode != null)
			XMLReflectionUtil.setAttribute(schedulerNode, "name", this,
					"schedulerName");

		Node processorNode = XMLHelpers.findChild(node, "processor");
		if (processorNode != null)
			XMLReflectionUtil.setAttribute(processorNode, "name", this,
					"processorName");

		Node dispatcherNode = XMLHelpers.findChild(node, "dispatcher");
		if (dispatcherNode != null)
			XMLReflectionUtil.setAttribute(dispatcherNode, "name",
					this, "dispatcherName");

		Node portsNode = XMLHelpers.findChild(node, "ports");
		if (portsNode != null) {
			Node[] inPorts = XMLHelpers.findChildren(portsNode, "in-port");
			for (Node inPort : inPorts)
				ports.add(new InPort(inPort));
			Node[] outPorts = XMLHelpers.findChildren(portsNode, "out-port");
			for (Node outPort : outPorts)
				ports.add(new OutPort(outPort));
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public String getName() {
		return name;
	}

	public List<Property> getProperties() {
		return properties;
	}
	
	@Override
	public void createMarkers(Object rootSourceProvider) {
		if (name == null || name.length() == 0)
			CiliaMarkerUtil.createErrorMarker("name can't be null or empty", JarRepoService.getInstance(), this);
		if (schedulerName == null || schedulerName.length() == 0)
			CiliaMarkerUtil.createErrorMarker("schedulerName can't be null or empty", JarRepoService.getInstance(), this);
		if (processorName == null || processorName.length() == 0)
			CiliaMarkerUtil.createErrorMarker("processorName can't be null or empty", JarRepoService.getInstance(), this);
		if (dispatcherName == null || dispatcherName.length() == 0)
			CiliaMarkerUtil.createErrorMarker("dispatcherName can't be null or empty", JarRepoService.getInstance(), this);
	}
}
