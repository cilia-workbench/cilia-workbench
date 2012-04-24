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

/**
 * 
 * @author Etienne Gandrille
 */
public class IdNamespace {

	private final Object id;
	private final Object namespace;

	public IdNamespace(Object id, Object namespace) {
		this.id = id;
		this.namespace = namespace;
	}

	public Object getId() {
		return id;
	}

	public Object getNamespace() {
		return namespace;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IdNamespace))
			return false;
		IdNamespace o = (IdNamespace) obj;

		if (!o.getId().equals(id))
			return false;
		if (!o.getNamespace().equals(namespace))
			return false;

		return true;
	}
}
