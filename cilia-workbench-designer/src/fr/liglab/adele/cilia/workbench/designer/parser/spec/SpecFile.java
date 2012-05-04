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
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.designer.parser.common.AbstractFile;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Mergeable;

/**
 * Represents a file, from a "physical" point of view. This file, which must
 * exists on the file system, can be well formed or not. If it is "well formed",
 * the model field is not null, and represents a model of the file.
 * 
 * @author Etienne Gandrille
 */
public class SpecFile extends AbstractFile<SpecModel> implements Mergeable {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.
	 * Mergeable#merge(java.lang.Object)
	 */
	@Override
	public List<Changeset> merge(Object other) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		SpecFile newInstance = (SpecFile) other;

		SpecModel oldModel = this.getModel();
		List<Changeset> result = MergeUtil.mergeObjectsFields(newInstance, this, "model");
		SpecModel newModel = this.getModel();

		// Because Spec Model is not displayed in tne view, here is a little
		// piece of code for handling this very special case...
		for (Changeset c : result) {

			// XML file becomes valid
			if (c.getOperation().equals(Operation.ADD) && c.getObject() == newModel)
				for (MediatorSpec ms : newModel.getMediatorSpecs())
					retval.add(new Changeset(Operation.ADD, ms));

			// XML file becomes invalid
			else if (c.getOperation().equals(Operation.REMOVE) && c.getObject() == oldModel)
				for (MediatorSpec ms : oldModel.getMediatorSpecs())
					retval.add(new Changeset(Operation.REMOVE, ms));

			// Other event, deeper in hierarchy
			else
				retval.add(c);
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}
}
