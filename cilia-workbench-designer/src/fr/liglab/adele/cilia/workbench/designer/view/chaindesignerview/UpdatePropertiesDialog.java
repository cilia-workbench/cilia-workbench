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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.view.UpdateComboKeyValueDialog;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.PropertyConstraint;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.NameProperty;

/**
 * 
 * @author Etienne Gandrille
 */
public class UpdatePropertiesDialog extends UpdateComboKeyValueDialog {

	private final static String title = "Properties constraint editor";
	private final static String message = "You can add equality constraints on the properties";
	private final static String keyLabel = "property";
	private final static String valueLabel = "constraint";
	private final static IInputValidator validator = getValidator();

	public UpdatePropertiesDialog(Shell parent, MediatorSpecRef mediator) {
		super(parent, getKeys(mediator), getInitialValues(mediator), title, message, keyLabel, valueLabel, validator,
				validator);
	}

	private static List<String> getKeys(MediatorSpecRef mediator) {
		List<String> names = new ArrayList<String>();
		for (NameProperty p : mediator.getPossibleConstraints())
			names.add(p.getName());
		return names;
	}

	private static Map<String, String> getInitialValues(MediatorSpecRef mediator) {
		Map<String, String> values = new HashMap<String, String>();
		for (PropertyConstraint p : mediator.getConstraints())
			values.put(p.getName(), p.getValue());
		return values;
	}

	private static IInputValidator getValidator() {
		return new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText == null || newText.trim().length() != 0)
					return null;
				return "text cant't be empty";
			}
		};
	}
}
