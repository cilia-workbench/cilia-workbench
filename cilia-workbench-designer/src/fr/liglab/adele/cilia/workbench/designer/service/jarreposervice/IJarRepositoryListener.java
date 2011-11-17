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

/**
 * The listener interface for receiving IJarRepository events. The class that is
 * interested in processing a IJarRepository event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addIJarRepositoryListener<code> method. When
 * the IJarRepository event occurs, that object's appropriate
 * method is invoked.
 */
public interface IJarRepositoryListener {

	/**
	 * Invoked when repository content update occurs.
	 */
	public void jarRepositoryContentUpdated();
}
