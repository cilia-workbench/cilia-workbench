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
package fr.liglab.adele.cilia.workbench.common.parser.element;

/**
 * For asking objects if they are Specification or implementation.
 * 
 * @author Etienne Gandrille
 */
public interface ComponentNatureAskable {

	/** Nature possibilities */
	public enum ComponentNature {

		SPEC("spec", "specification"), IMPLEM("implem", "implementation");

		private String shortName;
		private String longName;

		ComponentNature(String shortName, String longName) {
			this.shortName = shortName;
			this.longName = longName;
		}

		public String getShortName() {
			return shortName;
		}

		public String getLongName() {
			return longName;
		}
	}

	/**
	 * Ask the object nature : specification or implementation.
	 * 
	 * @return the object nature.
	 */
	ComponentNature getNature();
}
