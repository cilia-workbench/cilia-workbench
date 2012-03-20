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
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeleteMediatorHandler extends ChainDesignerHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(ViewUtil.getShell(event), "Handler", this.getClass().getName());
		return null;
	}

}
