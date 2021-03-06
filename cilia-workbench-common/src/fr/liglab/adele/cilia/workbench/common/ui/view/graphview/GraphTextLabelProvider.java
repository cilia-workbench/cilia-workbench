/**
 * Copyright 2012-2013 France Télécom 
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
package fr.liglab.adele.cilia.workbench.common.ui.view.graphview;

import org.eclipse.zest.core.viewers.EntityConnectionData;

/**
 * Computes text for the elements to be displayed in a graph : nodes and
 * bindings.
 * 
 * @author Etienne Gandrille
 */
public interface GraphTextLabelProvider {

	public String getNodeText(Object element, String defaultValue);

	public String getBindingText(EntityConnectionData element, String defaultValue);
}
