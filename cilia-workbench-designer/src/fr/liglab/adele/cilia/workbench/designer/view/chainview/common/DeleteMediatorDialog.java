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

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.SimpleListDialog;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeleteMediatorDialog extends SimpleListDialog {

	public DeleteMediatorDialog(Shell parent, Chain chain) {
		super(parent, "Remove mediator", "Select the mediator to be removed", chain.getMediators());
	}
}