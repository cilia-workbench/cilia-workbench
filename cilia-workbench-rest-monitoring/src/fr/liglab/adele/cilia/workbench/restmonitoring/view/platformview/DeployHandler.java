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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.SimpleListDialog;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;

/**
 * 
 * @author Etienne Gandrille
 */
public class DeployHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = ViewUtil.getShell(event);

		// Validity checking
		PlatformFile pfFile = getPlatformFileOrDisplayErrorDialog(event);
		if (pfFile == null)
			return null;

		// DSCilia file
		List<DSCiliaFile> values = DSCiliaRepoService.getInstance().getRepoContent();
		SimpleListDialog fileChooser = new SimpleListDialog(parentShell, "Cilia File chooser", "Please select a cilia file", values);
		if (fileChooser.open() != Window.OK)
			return null;
		DSCiliaFile file = (DSCiliaFile) (fileChooser.getResult()[0]);

		// DSCilia model
		DSCiliaModel model = file.getModel();
		if (model == null) {
			MessageDialog.openError(parentShell, "Error", "Cilia Model is in an INVALID state");
			return null;
		}

		// Finding resources
		List<String> resources = new ArrayList<String>();
		resources.add(file.getResource().getAssociatedResourcePath());
		for (DSCiliaChain chain : model.getChains()) {
			if (chain != null) {
				for (ComponentRef compoRef : chain.getComponents()) {
					ComponentDefinition def = compoRef.getReferencedComponentDefinition();
					if (def != null) {
						String resource = def.getPhysicalResourcePath();
						if (!resources.contains(resource))
							resources.add(resource);
					} else {
						MessageDialog.openError(parentShell, "Error", "A component definition is missing for " + compoRef.getId());
						return null;
					}
				}
			} else {
				MessageDialog.openError(parentShell, "Error", "A cilia Chain is in an INVALID state (null)");
				return null;
			}
		}

		// For tests
		String msg = "Liste des ressources à inclure dans le deployment package :\n";
		msg += Strings.arrayToString(resources.toArray(), "\n");
		MessageDialog.openInformation(parentShell, "DEBUG", msg);

		// TODO continue here !

		return null;
	}
}