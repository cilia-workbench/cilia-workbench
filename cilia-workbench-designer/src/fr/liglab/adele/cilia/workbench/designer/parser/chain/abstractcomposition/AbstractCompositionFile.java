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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractFile;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.common.Mergeable;

/**
 * Represents a file, from a "physical" point of view. This file, which must
 * exists on the file system, can be well formed or not. If it is "well formed",
 * the model field is not null, and represents a model of the file.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionFile extends AbstractFile<AbstractCompositionModel> implements Mergeable {

	public AbstractCompositionFile(File file) {
		super(file);

		try {
			model = new AbstractCompositionModel(file);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		AbstractCompositionFile newInstance = (AbstractCompositionFile) other;

		AbstractCompositionModel oldModel = this.getModel();
		List<Changeset> result = MergeUtil.mergeObjectsFields(newInstance, this, "model");
		AbstractCompositionModel newModel = this.getModel();

		// Because the Model file is not displayed in the view, here is a little
		// piece of code for handling this very special case...
		for (Changeset c : result) {

			// XML file becomes valid
			if (c.getOperation().equals(Operation.ADD) && c.getObject() == newModel)
				for (AbstractChain chain : newModel.getChains())
					retval.add(new Changeset(Operation.ADD, chain));

			// XML file becomes invalid
			else if (c.getOperation().equals(Operation.REMOVE) && c.getObject() == oldModel)
				for (AbstractChain chain : oldModel.getChains())
					retval.add(new Changeset(Operation.REMOVE, chain));

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
