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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.util.ArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
import fr.liglab.adele.cilia.workbench.designer.service.common.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * Indirection layer to {@link Dscilia}.
 * 
 * While DSCila represents ONLY valid files (parsed successfully), this class
 * represents valid and non valid DSCilia files. Non valid files have the
 * {@link #dscilia} field null.
 * 
 * @author Etienne Gandrille
 */
public class RepoElement {

	/**
	 * The dscilia model, which represents the file. This field is null, if
	 * there's an error while parsing the file.
	 */
	private Dscilia dscilia;

	/** The path on the file system, to the dscilia file. */
	private final String path;

	/**
	 * Instantiates a new repo element.
	 * 
	 * @param path
	 *            the path on the file system
	 * @param dscilia
	 *            the dscilia model
	 * @throws Exception
	 *             in case of error during dscilia file parsing.
	 */
	public RepoElement(String path) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(path));

		this.path = path;
		try {
			this.dscilia = new Dscilia(path);
		} catch (MetadataException e) {
			// The file exists, but there's an error while parsing it.
			this.dscilia = null;
			e.printStackTrace();
		}
	}

	/**
	 * Gets the logical abstraction of the file. Returns null if the file is not
	 * well formed.
	 * 
	 * @return the {@link Dscilia} object.
	 */
	public Dscilia getDscilia() {
		return dscilia;
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
	 * Merge another {@link RepoElement} into this {@link RepoElement}, and
	 * returns an array representing the differences.
	 * 
	 * @param newInstance
	 *            the other {@link RepoElement}, to be merged into the current
	 *            object.
	 * @return the {@link Changeset} list.
	 */
	protected Changeset[] merge(RepoElement newInstance) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		if (dscilia == null && newInstance.getDscilia() == null) {
			// do nothing
		} else if (dscilia != null && newInstance.getDscilia() != null) {
			for (Changeset c : dscilia.merge(newInstance.getDscilia()))
				retval.add(c);
		} else {
			retval.add(new Changeset(Operation.UPDATE, this));

			// DScilia becomes invalid
			if (dscilia != null) {
				for (Chain c : dscilia.getChains())
					retval.add(new Changeset(Operation.REMOVE, c));
			}

			dscilia = newInstance.getDscilia();

			// DScilia becomes valid
			if (dscilia != null) {
				for (Chain c : dscilia.getChains())
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