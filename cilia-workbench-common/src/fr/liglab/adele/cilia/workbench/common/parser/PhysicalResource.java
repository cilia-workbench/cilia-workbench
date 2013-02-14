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

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;

/**
 * Represents the physical artefact, and provides an abstraction layer to
 * interact with.
 * 
 * @author Etienne Gandrille
 */
public abstract class PhysicalResource implements Identifiable {

	public Object getId() {
		return getNameWithPath();
	}

	public abstract String getNameWithPath();

	public abstract String getName();

	public abstract File getJavaFile();

	public abstract InputStream getContentAsStream() throws IOException;

	public abstract boolean delete();
}
