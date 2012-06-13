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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice.AbstractCompositionsRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeleteBindingHandler extends ChainDesignerHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Chain chain = getChainDesignerView(event).getModel();
		if (chain != null) {
			DeleteBindingDialog window = new DeleteBindingDialog(ViewUtil.getShell(event), chain);
			if (window.open() == Window.OK) {
				Object[] objects = window.getResult();
				if (objects.length != 0) {
					Binding binding = (Binding) objects[0];
					try {
						AbstractCompositionsRepoService.getInstance().deleteBinding(chain, binding);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	private class DeleteBindingDialog extends ListDialog {

		public DeleteBindingDialog(Shell parent, Chain chain) {
			super(parent);

			setTitle("Remove binding");
			setMessage("Select the binding to be removed");
			setInput(chain.getBindings());

			setContentProvider(new ArrayContentProvider());
			setLabelProvider(new LabelProvider());
			setHelpAvailable(false);
		}
	}
}
