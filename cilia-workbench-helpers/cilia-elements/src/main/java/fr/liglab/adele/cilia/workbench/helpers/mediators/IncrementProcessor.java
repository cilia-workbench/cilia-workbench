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
package fr.liglab.adele.cilia.workbench.helpers.mediators;

import fr.liglab.adele.cilia.Data;

/**
 * Reads an integer and increments it with {@link #increment} value.
 * 
 * @author Etienne Gandrille
 */
public class IncrementProcessor extends IntegerProcessor {

	private Integer increment;

	public Data increment(Data data) {
		Integer content = getInteger(data);

		if (content != null) {
			Integer value = content + increment;
			data.setContent(value);
		}

		return data;
	}
}
