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

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.parser.chain.MediatorRef;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ParameterRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.ParameterDefinition;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.IInputValidatorBuilder;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.common.ui.widget.ComboKeyValueEditor;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class UpdateMediatorRefDialog extends WorkbenchDialog {

	private final static String windowTitle = "Mediator configuration";

	protected final static IInputValidator defaultValidator = IInputValidatorBuilder.getNonNullOrEmptyValidator();

	// Parameters editor
	private ComboKeyValueEditor paramEditor = null;
	private final String peMessage = "Parameters: you can configure the mediator element";
	private List<String> peKeys;
	private Map<String, String> peModel;
	private final String peKeyLabel = "parameter";
	private final String peValueLabel = "value";

	// Display prefix
	private static final String schedulerPrefix = "scheduler: ";
	private static final String processorPrefix = "processor: ";
	private static final String dispatcherPrefix = "dispatcher: ";

	protected UpdateMediatorRefDialog(Shell parent, MediatorRef mediator, Point initialSize) {
		super(parent, windowTitle, initialSize, true);
		peKeys = getConstraintKeys(mediator);
		peModel = getConstraintValues(mediator);
	}

	private static List<String> getConstraintKeys(MediatorRef mediator) {
		List<String> names = new ArrayList<String>();
		if (mediator.getReferencedComponentSchedulerParameters() != null)
			for (ParameterDefinition p : mediator.getReferencedComponentSchedulerParameters())
				names.add(schedulerPrefix + p.getName());
		if (mediator.getReferencedComponentProcessorParameters() != null)
			for (ParameterDefinition p : mediator.getReferencedComponentProcessorParameters())
				names.add(processorPrefix + p.getName());
		if (mediator.getReferencedComponentDispatcherParameters() != null)
			for (ParameterDefinition p : mediator.getReferencedComponentDispatcherParameters())
				names.add(dispatcherPrefix + p.getName());
		return names;
	}

	private static Map<String, String> getConstraintValues(MediatorRef mediator) {
		Map<String, String> values = new HashMap<String, String>();
		for (ParameterRef p : mediator.getSchedulerParameters())
			values.put(schedulerPrefix + p.getName(), p.getValue());
		for (ParameterRef p : mediator.getProcessorParameters())
			values.put(processorPrefix + p.getName(), p.getValue());
		for (ParameterRef p : mediator.getDispatcherParameters())
			values.put(dispatcherPrefix + p.getName(), p.getValue());
		return values;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		// Global layout
		container.setLayout(new GridLayout(1, false));

		// Sub class fields
		populateDialogArea(container);

		// Message area
		final Label messageArea = new Label(container, SWT.WRAP);
		messageArea.setText(peMessage);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Parameters editor
		paramEditor = new ComboKeyValueEditor(container, peKeys, peModel, peKeyLabel, peValueLabel);
		paramEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		paramEditor.setKeyValidator(defaultValidator);
		paramEditor.setValueValidator(defaultValidator);

		return container;
	}

	protected abstract void populateDialogArea(Composite container);

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		paramEditor.refresh();
	}

	public Map<String, String> getSchedulerParameters() {
		return getParameters(schedulerPrefix);
	}

	public Map<String, String> getProcessorParameters() {
		return getParameters(processorPrefix);
	}

	public Map<String, String> getDispatcherParameters() {
		return getParameters(dispatcherPrefix);
	}

	private Map<String, String> getParameters(String prefix) {
		Map<String, String> retval = new HashMap<String, String>();
		for (String key : peModel.keySet()) {
			if (key.startsWith(prefix))
				retval.put(key.substring(prefix.length()), peModel.get(key));
		}
		return retval;
	}
}
