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
package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import fr.liglab.adele.cilia.workbench.common.providers.ToggleSourceProvider;

/**
 * This boolean source provider is used for toolbar activation in the chain designer view.
 * 
 * @author Etienne Gandrille
 */
public class VariablesSourceProvider extends ToggleSourceProvider {

	/** The variable name. Must be unique */
	public final static String VARIABLE_NAME = "fr.liglab.adele.cilia.workbench.designer.chaindesignerview.toolbarEnable";

	/** A value for the boolean */
	private final static String TOOLBAR_ENABLE = "enable";

	/** A value for the boolean */
	private final static String TOOLBAR_DISABLE = "disable";

	/** The defaultValue. */
	private final static boolean defaultValue = false;

	/**
	 * Instantiates a new variables using the {@link ToggleSourceProvider} class.
	 */
	public VariablesSourceProvider() {
		super(VARIABLE_NAME, TOOLBAR_ENABLE, TOOLBAR_DISABLE, defaultValue);
	}
}
