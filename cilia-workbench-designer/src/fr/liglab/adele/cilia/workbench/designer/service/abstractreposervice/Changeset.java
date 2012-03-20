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
package fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a modification on a repository.
 * 
 * @author Etienne Gandrille
 */
public class Changeset {

	/**
	 * Operation.
	 */
	public enum Operation {
		/** ADD. */
		ADD,
		/** REMOVE. */
		REMOVE,
		/** UPDATE. */
		UPDATE
	};

	/** The operation. */
	private final Operation operation;

	/** The object. */
	private final Object object;

	/** The path to join the object in the complete model */
	private final List<Object> path = new ArrayList<Object>();

	/**
	 * Instantiates a new changeset.
	 * 
	 * @param operation
	 *            the operation
	 * @param object
	 *            the object
	 */
	public Changeset(Operation operation, Object object) {
		this.operation = operation;
		this.object = object;
		this.path.add(object);
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public List<Object> getPath() {
		return path;
	}

	public void pushPathElement(Object element) {
		path.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return operation + " " + object;
	}
}
