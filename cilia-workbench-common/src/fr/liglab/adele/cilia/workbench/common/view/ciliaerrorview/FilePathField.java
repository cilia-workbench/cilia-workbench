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

import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;

/**
 * The file path field, for rendering in the error view.
 * 
 * @author Etienne Gandrille
 */
public class FilePathField extends MarkerField {

	/** The field ID, used to store and find the attributes. */
	public final static String FIELD_ID = "fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.filepathfield";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.markers.MarkerField#getValue(org.eclipse.ui.views.markers.MarkerItem)
	 */
	@Override
	public String getValue(MarkerItem item) {
		return item.getAttributeValue(FIELD_ID, "");
	}
}
