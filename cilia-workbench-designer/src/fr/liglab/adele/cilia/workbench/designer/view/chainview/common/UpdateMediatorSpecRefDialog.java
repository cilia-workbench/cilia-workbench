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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.ui.editors.ComboKeyValueEditor;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.PropertyConstraint;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.PropertySpec;

/**
 * 
 * @author Etienne Gandrille
 */
public class UpdateMediatorSpecRefDialog extends UpdateMediatorRefDialog {

	private final static String message = "Properties: you can add selection constraint";

	// Parameters editor
	private ComboKeyValueEditor constraintEditor = null;
	private List<String> ceKeys;
	private Map<String, String> ceModel;
	private final String ceKeyLabel = "property";
	private final String ceValueLabel = "constraint";

	public UpdateMediatorSpecRefDialog(Shell parent, MediatorSpecRef mediator) {
		super(parent, mediator, new Point(500, 700));
		ceKeys = getConstraintKeys(mediator);
		ceModel = getConsltraintValues(mediator);
	}

	private static List<String> getConstraintKeys(MediatorSpecRef mediator) {
		List<String> names = new ArrayList<String>();
		for (PropertySpec p : mediator.getPossibleConstraints())
			names.add(p.getName());
		return names;
	}

	private static Map<String, String> getConsltraintValues(MediatorSpecRef mediator) {
		Map<String, String> values = new HashMap<String, String>();
		for (PropertyConstraint p : mediator.getConstraints())
			values.put(p.getName(), p.getValue());
		return values;
	}

	@Override
	protected void populateDialogArea(Composite container) {

		// message
		final Label messageArea = new Label(container, SWT.WRAP);
		messageArea.setText(message);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// editor
		constraintEditor = new ComboKeyValueEditor(container, ceKeys, ceModel, ceKeyLabel, ceValueLabel);
		constraintEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		constraintEditor.setKeyValidator(defaultValidator);
		constraintEditor.setValueValidator(defaultValidator);
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		constraintEditor.refresh();
	}

	public Map<String, String> getConstraints() {
		return ceModel;
	}
}
