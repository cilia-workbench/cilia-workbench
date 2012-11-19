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
package fr.liglab.adele.cilia.workbench.designer.parser.element.spec;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.element.Scheduler;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;

/**
 * 
 * @author Etienne Gandrille
 */
public class SchedulerSpec extends Scheduler implements Mergeable {

	public static final String XML_NODE_NAME = "scheduler";

	public SchedulerSpec(Node node) throws CiliaException {
		super(new ParameterListSpec(node));
	}

	@Override
	public String toString() {
		return XML_NODE_NAME;
	}

	public static Node createXMLParameter(Document document, Node mediatorSpec, String param) {
		return ComponentPartSpecHelper.createXMLParameter(document, mediatorSpec, param, XML_NODE_NAME);
	}
}
