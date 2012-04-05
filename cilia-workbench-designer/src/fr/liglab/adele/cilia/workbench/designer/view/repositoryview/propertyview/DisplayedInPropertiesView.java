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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview;

/**
 * This interface is used for selecting classes which attributes should be displayed in the properties view using {@link PropertiesAdapter}.
 * 
 * In the plugin.xml file, the following snippet is used to renderer all marked classes in the properties view :
 * 	<extension point="org.eclipse.core.runtime.adapters">
 *		<factory
 *			adaptableType="fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView"
 *			class="fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.PropertiesAdapter">
 *			<adapter type="org.eclipse.ui.views.properties.IPropertySource"></adapter>
 *		</factory>
 *	</extension>
 * 
 * @author Etienne Gandrille
 */
public interface DisplayedInPropertiesView {
	// nothing, because only used to mark classes !
}
