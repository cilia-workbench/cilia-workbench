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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.jarrepositoryview;

import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;

/**
 * 
 * @author Etienne Gandrille
 */
class StreamFromFileStorage implements IStorage {

	private PhysicalResource resource;

	StreamFromFileStorage(PhysicalResource resource) {
		this.resource = resource;
	}

	public InputStream getContents() throws CoreException {
		try {
			return resource.getContentAsStream();
		} catch (CiliaException e) {
			e.printStackTrace();
			return null;
		}
	}

	public IPath getFullPath() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getName() {
		return resource.getNameWithPath();
	}

	public boolean isReadOnly() {
		return true;
	}
}