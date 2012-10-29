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

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaWarning;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;

/**
 * A base class for implementing objects having a name and a namespace.
 * 
 * This class is semantically different from {@link NameNamespaceID}.
 * {@link NameNamespace} represents a base class whereas {@link NameNamespaceID}
 * represents an id, for a class implementing {@link NameNamespace}.
 * 
 * @author Etienne Gandrille
 */
public abstract class NameNamespace implements ErrorsAndWarningsFinder, Identifiable {

	private String name;
	private String namespace;

	public NameNamespace() {
	}

	public NameNamespace(String name, String namespace) {
		this.name = name;
		this.namespace = namespace;
	}

	@Override
	public NameNamespaceID getId() {
		return new NameNamespaceID(name, namespace);
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	/**
	 * The qualified name is composed by the namespace and the name. If the
	 * namespace is unavailable, this function returns the name.
	 * 
	 * @return the qualified name
	 */
	public String getQualifiedName() {
		return new NameNamespaceID(name, namespace).getQualifiedName();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		CiliaFlag e2 = CiliaWarning.checkStringNotNullOrEmpty(this, namespace, "namespace");

		return CiliaFlag.generateTab(e1, e2);
	}
}