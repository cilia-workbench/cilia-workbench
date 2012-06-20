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
package fr.liglab.adele.cilia.workbench.designer.parser.element.common;

/**
 * 
 * @author Etienne Gandrille
 */
public enum Cardinality {

	OPTIONAL(0, 1), OPTIONAL_MULTI(0, -1), MANDATORY(1, 1), MANDATORY_MULTI(1, -1);

	private int min;
	private int max;

	Cardinality(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMinValue() {
		return min;
	}

	public int getMaxValue() {
		return max;
	}

	public boolean isInfiniteBoundary() {
		return (max == -1);
	}

	public String stringId() {
		String minName = getString(min);
		String maxName = getString(max);
		return minName + "..." + maxName;
	}

	private String getString(int i) {
		// infinite symbol : "\u221E"
		if (i != -1)
			return Integer.toString(i);
		return "n";
	}

	public static Cardinality getCardinality(String stringId) {
		for (Cardinality c : Cardinality.values())
			if (c.stringId().equals(stringId))
				return c;
		throw new IllegalArgumentException(stringId + " doesn't represents a valid cardinality.");
	}
}
