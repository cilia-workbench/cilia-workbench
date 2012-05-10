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
 * A few processors, using Integers.
 * 
 * @author Etienne Gandrille
 */
public class MathProcessor extends IntegerProcessor {

	private Integer sum = 0;
	private int counter = 0;

	private void process(Data data) {
		Integer content = getInteger(data);

		if (content != null)
			sum += content;
		counter++;
	}

	/**
	 * Method used by the sum processor.
	 * 
	 * @param data
	 * @return
	 */
	public Data sum(Data data) {
		process(data);
		data.setContent(sum);
		return data;
	}

	/**
	 * Method used by the average processor.
	 * 
	 * @param data
	 * @return
	 */
	public Data average(Data data) {
		process(data);
		data.setContent(sum / counter);
		return data;
	}
}
