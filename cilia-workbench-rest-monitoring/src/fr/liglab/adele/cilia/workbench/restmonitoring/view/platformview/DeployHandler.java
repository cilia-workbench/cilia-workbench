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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeployHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Validity checking
		// =================
		PlatformFile file = getPlatformFileOrDisplayErrorDialog(event);
		if (file == null)
			return null;

		// Display mock
		// ============

		StringBuilder sb = new StringBuilder();

		sb.append("The following artifacts will be deployed to\n");
		sb.append(file.getModel().getPlatformID());
		sb.append("\n\n");

		List<CiliaJarFile> list = CiliaJarRepoService.getInstance().getRepoContent();
		for (CiliaJarFile jar : list) {
			sb.append("  * ");
			sb.append(jar.toString());
			sb.append("\n");
		}

		boolean result = (boolean) MessageDialog.openConfirm(ViewUtil.getShell(event), "Deploy artifacts to platform", sb.toString());

		if (result) {
			MessageDialog.openInformation(ViewUtil.getShell(event), "Success", "Artifacts deployed !");
		}

		return null;
	}
}