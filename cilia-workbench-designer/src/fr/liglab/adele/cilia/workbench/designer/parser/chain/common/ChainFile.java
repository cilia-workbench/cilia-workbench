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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.parser.chain.IChain;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainFile<ModelType extends XMLChainModel<? extends XMLChain>> extends AbstractFile<ModelType> implements Mergeable {

	public ChainFile(File file) {
		super(file);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		@SuppressWarnings("unchecked")
		ChainFile<XMLChainModel<XMLChain>> newInstance = (ChainFile<XMLChainModel<XMLChain>>) other;

		ModelType oldModel = getModel();
		List<Changeset> result = MergeUtil.mergeObjectsFields(newInstance, this, "model");
		ModelType newModel = getModel();

		// Because the Model file is not displayed in the view, here is a little
		// piece of code for handling this very special case...
		for (Changeset c : result) {

			// XML file becomes valid
			if (c.getOperation().equals(Operation.ADD) && c.getObject() == newModel)
				for (IChain chain : newModel.getChains())
					retval.add(new Changeset(Operation.ADD, chain));

			// XML file becomes invalid
			else if (c.getOperation().equals(Operation.REMOVE) && c.getObject() == oldModel)
				for (IChain chain : oldModel.getChains())
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
