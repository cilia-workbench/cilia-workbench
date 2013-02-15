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
package fr.liglab.adele.cilia.workbench.common.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;

/**
 * Represents the physical artefact, and provides an abstraction layer to
 * interact with.
 * 
 * @author Etienne Gandrille
 */
public abstract class PhysicalResource implements Identifiable {

	/**
	 * Returns an id, which MUST be computed using the underlying physical
	 * resource. This id MUST be human readable.
	 * 
	 * @return an id
	 */
	public abstract String getId();

	/**
	 * A human readable name, for display purpose. This name must NOT be used as
	 * an id because it may NOT be unique.
	 * 
	 * @return A human readable name, for display purpose.
	 */
	public abstract String getDisplayName();

	/**
	 * The file FULLY represented by the resource. This method MUST throw an
	 * exception if the file can be linked to more than one resource.
	 * 
	 * @see #getAssociatedResourceFile()
	 * @return
	 * @throws InvalidObjectException
	 */
	public abstract File getExactResourceFile() throws InvalidObjectException;

	/**
	 * The underlying file which contains the resource. This {@link File} ALWAYS
	 * exists, and can be used by one or more resources.
	 * 
	 * @see #getExactResourceFile()
	 * @return
	 */
	public abstract File getAssociatedResourceFile();

	public String getAssociatedResourcePath() {
		return getAssociatedResourceFile().getAbsolutePath();
	}

	public abstract InputStream getContentAsStream() throws IOException;

	/**
	 * Deletes the underlying resource.
	 * 
	 * @return
	 * @see #getExactResourceFile()
	 * @throws InvalidObjectException
	 *             if the resource MUSTN'T be destroyed.
	 */
	public boolean delete() throws InvalidObjectException {
		return getExactResourceFile().delete();
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
