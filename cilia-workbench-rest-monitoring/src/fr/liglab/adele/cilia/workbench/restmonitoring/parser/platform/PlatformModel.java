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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformModel implements DisplayedInPropertiesView, Mergeable, ErrorsAndWarningsFinder {
		
	public static final String XML_NODE_NAME = "cilia-platform";

	private String host;
	private String port;
	private File file;

	private List<PlatformChain> chains = new ArrayList<PlatformChain>();
	
	public PlatformModel(File file) throws CiliaException {
		this.file = file;

		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		ReflectionUtil.setAttribute(root, "host", this, "host");
		ReflectionUtil.setAttribute(root, "port", this, "port");		
	}

	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return port;
	}
	
	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, XML_NODE_NAME);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		PlatformModel newInstance = (PlatformModel) other;

		retval.addAll(MergeUtil.computeUpdateChangeset(newInstance, this, "host", "port"));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}
	
	public static String hostValidator(String host) {
		if (host.isEmpty())
			return "Host can't be empty";
		return null;
	}
	
	public static String portValidator(String port) {
		if (port.isEmpty())
			return "Port can't be empty";
		
		try {
			Integer portValue = Integer.valueOf(port);
			if (portValue < 10 || portValue > 65535)
				return "Wrong port number";
		} catch (NumberFormatException e) {
			return "Wrong port format";
		}
		
		return null;
	}
	
	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = null;
		CiliaFlag e2 = null;
		
		if (hostValidator(host) != null)
			e1 = new CiliaError(hostValidator(host), this);
		if (portValidator(port) != null)
			e1 = new CiliaError(portValidator(port), this);
				
		return CiliaFlag.generateTab(e1, e2);
	}
}
