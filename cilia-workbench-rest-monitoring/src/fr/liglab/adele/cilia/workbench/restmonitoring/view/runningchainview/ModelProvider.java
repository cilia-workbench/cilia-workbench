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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Etienne Gandrille
 */
public enum ModelProvider {
	INSTANCE;

	private List<StateVar> stateVars;

	private ModelProvider() {
		stateVars = new ArrayList<StateVar>();

		Random random = new Random();

		stateVars.add(new StateVar("msgCounter", true, Integer.toString(random.nextInt(30))));
		stateVars.add(new StateVar("enabledVar", true, Integer.toString(random.nextInt(17))));
		stateVars.add(new StateVar("disabledVar", false, ""));
	}

	public List<StateVar> getStateVar() {
		return stateVars;
	}
}
