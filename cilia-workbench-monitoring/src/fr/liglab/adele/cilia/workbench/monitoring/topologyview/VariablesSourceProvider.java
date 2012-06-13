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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview;

import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;

/**
 * Handles the reload require variable using a SourceProvider. 
 * @author Etienne Gandrille
 */
public class VariablesSourceProvider extends ToggleSourceProvider {

	public final static String RELOAD_REQUIRED_VARIABLE = "fr.liglab.adele.cilia.workbench.monitoring.topologyview.reloadRequired";
	private final static String RELOAD_REQUIRED = "required";
	private final static String RELOAD_NOT_REQUIRED = "not required";
	private final static boolean defaultValue = true;

	public VariablesSourceProvider() {
		super(RELOAD_REQUIRED_VARIABLE, RELOAD_REQUIRED, RELOAD_NOT_REQUIRED, defaultValue);
	}
}
