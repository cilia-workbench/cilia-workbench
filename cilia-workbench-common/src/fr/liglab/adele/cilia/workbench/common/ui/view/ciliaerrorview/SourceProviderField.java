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
package fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview;

import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;

/**
 * The source provider field, for rendering in the error view.
 * 
 * @author Etienne Gandrille
 */
public class SourceProviderField extends MarkerField {

	/** The field ID, used to store and find the attributes. */
	public final static String FIELD_ID = "fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview.sourceproviderfield";

	@Override
	public String getValue(MarkerItem item) {
		try {
			String value = item.getAttributeValue(FIELD_ID, "");
			if (value != null)
				return value;
			throw new RuntimeException();
		} catch (Exception e) {
			return item.getAttributeValue(FIELD_ID, "");
		}
	}
}
