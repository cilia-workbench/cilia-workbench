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
package fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview;

import org.eclipse.ui.views.markers.MarkerSupportView;

/**
 * The error View.
 * 
 * @author Etienne Gandrille
 */
public class CiliaErrorView extends MarkerSupportView {

	/** View ID */
	public static final String VIEW_ID = "fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview";

	/**
	 * The content generator is used to retrieve the marker type and the fileds to be displayed. It is defined in the
	 * plugin.xml file.
	 */
	public static final String CONTENT_GENERATOR_ID = "fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.generator";

	/**
	 * Instantiates a new error view.
	 */
	public CiliaErrorView() {
		super(CONTENT_GENERATOR_ID);
	}

}
