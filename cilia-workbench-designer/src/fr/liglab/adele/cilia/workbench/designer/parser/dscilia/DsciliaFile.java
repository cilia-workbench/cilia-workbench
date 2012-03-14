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
package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

/**
 * Represents a Dscilia file, from a "physical" point of view. This file, which
 * must exists on the file system, can be well formed or not.
 * If it is "well formed", the dscilia field is not null, and represents a model of the file.
 */
public class DsciliaFile {

	/** Dscilia model. Can be null, if the file is not well formed.*/
	private DsciliaModel model;

	/** Path on the file system. */
	private String path;

	/**
	 * Instantiates a new DsciliaFile
	 * 
	 * @param path
	 *            the path on the file system.
	 * @param model
	 *            the dscilia
	 */
	public DsciliaFile(String path) {
		this.path = path;
		
		try {
			model = new DsciliaModel(path);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

	/**
	 * Gets the dscilia.
	 * 
	 * @return the dscilia
	 */
	public DsciliaModel getModel() {
		return model;
	}

	/**
	 * Gets the file path.
	 * 
	 * @return the file path
	 */
	public String getFilePath() {
		return path;
	}

	/**
	 * Merge.
	 * 
	 * @param newInstance
	 *            the new instance
	 * @return the changeset[]
	 */
	public Changeset[] merge(DsciliaFile newInstance) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		if (model == null && newInstance.getModel() == null) {
			// do nothing
		} else if (model != null && newInstance.getModel() != null) {
			for (Changeset c : model.merge(newInstance.getModel()))
				retval.add(c);
		} else {
			retval.add(new Changeset(Operation.UPDATE, this));

			// DScilia becomes invalid
			if (model != null) {
				for (Chain c : model.getChains())
					retval.add(new Changeset(Operation.REMOVE, c));
			}

			model = newInstance.getModel();

			// DScilia becomes valid
			if (model != null) {
				for (Chain c : model.getChains())
					retval.add(new Changeset(Operation.ADD, c));
			}
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.getFileName(path);
	}
}