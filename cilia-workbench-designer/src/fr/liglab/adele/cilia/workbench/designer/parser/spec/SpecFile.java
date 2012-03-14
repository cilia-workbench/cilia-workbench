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

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;

public class SpecFile {

	/** specFile model. Can be null, if the file is not well formed.*/
	private SpecModel model;

	/** Path on the file system. */
	private String path;

	/**
	 * Instantiates a new DsciliaFile
	 * 
	 * @param path
	 *            the path on the file system.
	 * @param dscilia
	 *            the dscilia
	 */
	public SpecFile(String path) {
		this.path = path;
		
		try {
			model = new SpecModel(path);
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
	public SpecModel getModel() {
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
