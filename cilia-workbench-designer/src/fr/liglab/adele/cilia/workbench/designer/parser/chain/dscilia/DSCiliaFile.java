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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia;

import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChainFile;

/**
 * Represents a file, from a "physical" point of view. This file, which must
 * exists on the file system, can be well formed or not. If it is "well formed",
 * the model field is not null, and represents a model of the file.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaFile extends XMLChainFile<DSCiliaModel, DSCiliaChain> {

	public DSCiliaFile(PhysicalResource file) {
		super(file);

		try {
			model = new DSCiliaModel(file);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

}
