/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Dscilia;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * RepoElement.
 */
public class RepoElement {

	/** The dscilia. */
	Dscilia dscilia;

	/** The path on the file system. */
	String path;

	/**
	 * Instantiates a new repo element.
	 * 
	 * @param path
	 *            the path
	 * @param dscilia
	 *            the dscilia
	 */
	public RepoElement(String path, Dscilia dscilia) {
		this.path = path;
		this.dscilia = dscilia;
	}

	/**
	 * Gets the dscilia.
	 * 
	 * @return the dscilia
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
	 * Merge.
	 * 
	 * @param newInstance
	 *            the new instance
	 * @return the changeset[]
	 */
	public Changeset[] merge(RepoElement newInstance) {
		
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		
		if (dscilia == null && newInstance.getDscilia() == null) {
			// do nothing
		}
		else if (dscilia != null && newInstance.getDscilia() != null) {
			for (Changeset c : dscilia.merge(newInstance.getDscilia()))
				retval.add(c);
		}
		else {
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