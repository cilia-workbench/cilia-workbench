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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.ChainFile;
import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesViewWithForward;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformFile extends ChainFile<PlatformModel, PlatformChain> implements Mergeable, DisplayedInPropertiesViewWithForward {

	public PlatformFile(PhysicalResource file) {
		super(file);

		try {
			model = new PlatformModel(file, this);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		PlatformFile newInstance = (PlatformFile) other;

		PlatformModel oldModel = getModel();
		List<Changeset> result = MergeUtil.mergeObjectsFields(newInstance, this, "model");
		PlatformModel newModel = getModel();

		// Because the Model file is not displayed in the view, here is a little
		// piece of code for handling this very special case...
		for (Changeset c : result) {

			// XML file becomes valid
			if (c.getOperation().equals(Operation.ADD) && c.getObject() == newModel) {
				// the new model is empty... nothing to add inside the model
				retval.add(new Changeset(Operation.UPDATE, this));
			}

			// XML file becomes invalid
			else if (c.getOperation().equals(Operation.REMOVE) && c.getObject() == oldModel) {
				for (PlatformChain chain : oldModel.getChains())
					retval.add(new Changeset(Operation.REMOVE, chain));
				retval.add(new Changeset(Operation.UPDATE, this));
			}

			// Other event, deeper in hierarchy
			else
				retval.add(c);
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	public Object getObjectForComputingProperties() {
		if (model == null)
			return this;
		else
			return model;
	}
}
