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
package fr.liglab.adele.cilia.workbench.designer.parser.common;

import fr.liglab.adele.cilia.workbench.common.misc.StringUtil;

/**
 * Represents a file, from a "physical" point of view. This file, which must exists on the file system, can be well
 * formed or not. If it is "well formed", the model field is not null, and represents a model of the file.
 * 
 * @param <ModelType>
 *            the model type
 * 
 * @author Etienne Gandrille
 */
public class AbstractFile<ModelType> {

	/** Path on the file system. */
	private String path;

	/** The model object, which represents the file. */
	protected ModelType model;

	/**
	 * Instantiates a new abstract file.
	 * 
	 * @param path
	 *            the path
	 */
	public AbstractFile(String path) {
		this.path = path;
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
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public ModelType getModel() {
		return model;
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
