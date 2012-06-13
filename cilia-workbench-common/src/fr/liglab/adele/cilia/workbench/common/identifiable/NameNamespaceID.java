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

import com.google.common.base.Strings;

/**
 * Helper class, which represents an id from the {@link Identifiable} point of
 * view.
 * 
 * This class is semantically different from {@link NameNamespace}.
 * {@link NameNamespace} represents a base class whereas {@link NameNamespaceID}
 * represents an id, for a class implementing {@link Identifiable}.
 * 
 * @author Etienne Gandrille
 */
public class NameNamespaceID {

	private final String name;
	private final String namespace;

	/** separator used between name and namespace, for display purpose */
	private final String separator = ".";

	public NameNamespaceID(String name, String namespace) {
		this.name = name;
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof NameNamespaceID))
			return false;
		NameNamespaceID o = (NameNamespaceID) obj;

		if (!Strings.nullToEmpty(o.getName()).equals(Strings.nullToEmpty(name)))
			return false;
		if (!Strings.nullToEmpty(o.getNamespace()).equals(Strings.nullToEmpty(namespace)))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * The qualified name is composed by the namespace and the name. If the
	 * namespace is unavailable, this function returns the name.
	 * 
	 * @return the qualified name
	 */
	public String getQualifiedName() {
		if (namespace == null || namespace.length() == 0)
			return name;
		else
			return namespace + separator + name;
	}
}
