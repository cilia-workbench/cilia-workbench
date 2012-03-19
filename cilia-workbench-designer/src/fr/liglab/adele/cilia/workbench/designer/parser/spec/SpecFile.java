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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.designer.parser.common.AbstractFile;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * Represents a file, from a "physical" point of view. This file, which must exists on the file system, can be well
 * formed or not. If it is "well formed", the model field is not null, and represents a model of the file.
 * 
 * @author Etienne Gandrille
 */
public class SpecFile extends AbstractFile<SpecModel> {

	/**
	 * Instantiates a new spec file.
	 * 
	 * @param path
	 *            the path
	 */
	public SpecFile(String path) {
		super(path);

		try {
			model = new SpecModel(path);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

	/**
	 * Merge.
	 * 
	 * @param newInstance
	 *            the new instance
	 * @return the changeset[]
	 */
	public Changeset[] merge(SpecFile newInstance) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		if (model == null && newInstance.getModel() == null) {
			// do nothing
		} else if (model != null && newInstance.getModel() != null) {
			for (Changeset c : model.merge(newInstance.getModel()))
				retval.add(c);
		} else {
			retval.add(new Changeset(Operation.UPDATE, this));

			// becomes invalid
			if (model != null) {
				for (MediatorSpec ms : model.getMediatorSpecs())
					retval.add(new Changeset(Operation.REMOVE, ms));
			}

			model = newInstance.getModel();

			// becomes valid
			if (model != null) {
				for (MediatorSpec ms : model.getMediatorSpecs())
					retval.add(new Changeset(Operation.ADD, ms));
			}
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}
}