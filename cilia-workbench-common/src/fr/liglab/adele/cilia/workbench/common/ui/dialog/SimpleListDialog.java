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
package fr.liglab.adele.cilia.workbench.common.ui.dialog;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class SimpleListDialog extends ListDialog {

	public SimpleListDialog(Shell parent, String title, String message, List<?> input) {
		super(parent);

		setTitle(title);
		setMessage(message);
		setInput(input);

		setContentProvider(new ArrayContentProvider());
		setLabelProvider(new LabelProvider());
		setHelpAvailable(false);
	}

	public SimpleListDialog(Shell parent, String title, String message, List<?> input, Object... initSelection) {
		this(parent, title, message, input);
		setInitialSelections(initSelection);
	}
}
