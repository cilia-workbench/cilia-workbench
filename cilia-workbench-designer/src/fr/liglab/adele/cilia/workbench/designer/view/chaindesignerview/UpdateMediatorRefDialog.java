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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.ui.dialog.WorkbenchDialog;
import fr.liglab.adele.cilia.workbench.common.ui.editors.ComboKeyValueEditor;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Parameter;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericParameter;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class UpdateMediatorRefDialog extends WorkbenchDialog {

	private final static String windowTitle = "Mediator configuration";

	protected final static IInputValidator defaultValidator = getNonNullOrEmptyValidator();

	// Parameters editor
	private ComboKeyValueEditor paramEditor = null;
	private final String peMessage = "Parameters: you can configure the mediator element";
	private List<String> peKeys;
	private Map<String, String> peModel;
	private final String peKeyLabel = "parameter";
	private final String peValueLabel = "value";

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
		if (mediator.getReferencedObjectSchedulerParameters() != null)
			for (GenericParameter p : mediator.getReferencedObjectSchedulerParameters())
				names.add(schedulerPrefix + p.getName());
		if (mediator.getReferencedObjectProcessorParameters() != null)
			for (GenericParameter p : mediator.getReferencedObjectProcessorParameters())
				names.add(processorPrefix + p.getName());
		if (mediator.getReferencedObjectDispatcherParameters() != null)
			for (GenericParameter p : mediator.getReferencedObjectDispatcherParameters())
				names.add(dispatcherPrefix + p.getName());
		return names;
	}

	private static Map<String, String> getConstraintValues(MediatorRef mediator) {
		Map<String, String> values = new HashMap<String, String>();
		for (Parameter p : mediator.getSchedulerParameters())
			values.put(schedulerPrefix + p.getName(), p.getValue());
		for (Parameter p : mediator.getProcessorParameters())
			values.put(processorPrefix + p.getName(), p.getValue());
		for (Parameter p : mediator.getDispatcherParameters())
			values.put(dispatcherPrefix + p.getName(), p.getValue());
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
	 */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		paramEditor.refresh();
	}

	private static IInputValidator getNonNullOrEmptyValidator() {
		return new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText == null || newText.trim().length() != 0)
					return null;
				return "text cant't be empty";
			}
		};
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
