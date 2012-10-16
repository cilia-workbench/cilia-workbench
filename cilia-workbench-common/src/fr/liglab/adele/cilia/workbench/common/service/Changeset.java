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
package fr.liglab.adele.cilia.workbench.common.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a modification in a repository. A {@link Changeset} is composed
 * by:
 * <ul>
 * <li>An {@link Operation}</li>
 * <li>The object impacted by the Changeset</li>
 * <li>A path, to join the object in the Object tree</li>
 * </ul>
 * 
 * @author Etienne Gandrille
 */
public class Changeset {

	public enum Operation {
		ADD, REMOVE, UPDATE
	};

	// fields
	private final Operation operation;
	private final Object object;
	private final List<Object> path = new ArrayList<Object>();

	public Changeset(Operation operation, Object object) {
		this.operation = operation;
		this.object = object;
		this.path.add(object);
	}

	public Object getObject() {
		return object;
	}

	public Operation getOperation() {
		return operation;
	}

	public List<Object> getPath() {
		return path;
	}

	public void pushPathElement(Object element) {
		path.add(element);
	}

	@Override
	public String toString() {
		return operation + " " + object;
	}

	public static void displayChangeset(List<Changeset> changesets) {
		System.out.println("Liste des changesets");
		for (Changeset c : changesets)
			System.out.println(" * " + c);
	}
}
