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
package fr.liglab.adele.cilia.workbench.common.identifiable;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformID {

	private final String host;
	private final String port;

	public PlatformID(Node root) {
		host = XMLHelpers.findAttributeValueOrEmpty(root, "host");
		port = XMLHelpers.findAttributeValueOrEmpty(root, "port");
	}

	public PlatformID(String host, String port) {
		this.host = Strings.nullToEmpty(host);
		this.port = Strings.nullToEmpty(port);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return Integer.valueOf(port);
	}

	public String isValid() {
		if (hostValidator(host) != null)
			return hostValidator(host);
		if (portValidator(port) != null)
			return portValidator(port);
		return null;
	}

	private static String hostValidator(String host) {
		if (host.isEmpty())
			return "Host can't be empty";
		return null;
	}

	private static String portValidator(String port) {
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
	public String toString() {
		if (!host.isEmpty() && !port.isEmpty())
			return host + ":" + port;
		else if (!host.isEmpty())
			return host;
		else if (!port.isEmpty())
			return port;
		else
			return "<unknown>";
	}
}
