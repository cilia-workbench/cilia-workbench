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
package fr.liglab.adele.cilia.workbench.common.parser;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * Represents a file, from a "physical" point of view. This file, which must
 * exists on the file system, can be well formed or not. If it is "well formed",
 * the model field is not null, and represents a model of the file.
 * 
 * @param <ModelType>
 *            the model type
 * 
 * @author Etienne Gandrille
 */
public class AbstractFile<ModelType> implements ErrorsAndWarningsFinder, DisplayedInPropertiesView, Identifiable {

	private PhysicalResource resource;

	/** The model object, which represents the file. */
	protected ModelType model;

	protected String XMLerrorMessage = null;

	public AbstractFile(PhysicalResource resource) {
		this.resource = resource;
	}

	@Override
	public Object getId() {
		return resource.getId();
	}

	public String getFilename() {
		return resource.getNameWithPath();
	}

	public PhysicalResource getResource() {
		return resource;
	}

	public ModelType getModel() {
		return model;
	}

	@Override
	public String toString() {
		return Strings.nullToEmpty(resource.getName());
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		CiliaFlag e1 = null;
		if (model == null) {
			String message;
			if (XMLerrorMessage == null)
				message = "XML file can't be null";
			else
				message = XMLerrorMessage;
			e1 = new CiliaError(message, this);
		}

		CiliaError.checkNotNull(this, model, "XML file");

		if (model != null && model instanceof ErrorsAndWarningsFinder) {
			for (CiliaFlag flag : ((ErrorsAndWarningsFinder) model).getErrorsAndWarnings()) {
				// little hack... to forward responsability...
				if (flag.getSourceProvider() == model) {
					retval.add(flag.changeSourceProvider(this));
				} else {
					retval.add(flag);
				}
			}
		}

		return CiliaFlag.generateTab(retval, e1);
	}
}
