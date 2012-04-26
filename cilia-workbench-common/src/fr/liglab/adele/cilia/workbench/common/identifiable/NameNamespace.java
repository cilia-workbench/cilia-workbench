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
 * 
 * @author Etienne Gandrille
 */
public class NameNamespace {

	private final String name;
	private final String namespace;

	public NameNamespace(String name, String namespace) {
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
		if (obj == null || !(obj instanceof NameNamespace))
			return false;
		NameNamespace o = (NameNamespace) obj;

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
}
