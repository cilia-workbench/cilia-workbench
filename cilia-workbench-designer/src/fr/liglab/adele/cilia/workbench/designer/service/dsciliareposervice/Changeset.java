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

/**
 * Represents a modification on a repository.
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
}
