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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.CiliaRestHelper;

/**
 * 
 * @author Etienne Gandrille
 */
public class FetchChainListHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Validity checking
		// =================
		PlatformFile file = getPlatformFileOrDisplayErrorDialog(event);
		if (file == null)
			return null;
		PlatformID platformID = file.getModel().getPlatformID();

		// REST
		// ====
		String[] chains;
		try {
			chains = CiliaRestHelper.getChainsList(platformID);
		} catch (CiliaException e) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", e.getMessage());
			return null;
		}

		// Model Update
		// ============

		PlatformRepoService.getInstance().updateChains(file.getFilename(), chains);

		return null;
	}
}
