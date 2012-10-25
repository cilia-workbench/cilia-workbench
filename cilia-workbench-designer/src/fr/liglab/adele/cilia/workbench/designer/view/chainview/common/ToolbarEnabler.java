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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.common;

import fr.liglab.adele.cilia.workbench.common.misc.ToggleSourceProvider;

/**
 * Toggle variable used to enable and disable toolbar buttons.
 * 
 * @author Etienne Gandrille
 */
public class ToolbarEnabler extends ToggleSourceProvider {

	public final static String VARIABLE_NAME = "fr.liglab.adele.cilia.workbench.designer.view.chainview.common.ToolbarEnabler";
	private final static String TOOLBAR_ENABLE = "enable";
	private final static String TOOLBAR_DISABLE = "disable";
	private final static boolean defaultValue = false;

	public ToolbarEnabler() {
		super(VARIABLE_NAME, TOOLBAR_ENABLE, TOOLBAR_DISABLE, defaultValue);
	}
}
