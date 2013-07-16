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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class ReloadHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = ViewUtil.getShell(event);
		RunningChainView view = (RunningChainView) ViewUtil.findViewWithId(event, RunningChainView.VIEW_ID);
		if (view == null)
			MessageDialog.openError(parentShell, "Error", "Can't find chain view");
		PlatformChain chain = view.getModel();
		if (chain == null)
			MessageDialog.openError(parentShell, "Error", "Chain is undefined");
		PlatformModel platform = chain.getPlatform();
		if (platform == null)
			MessageDialog.openError(parentShell, "Error", "Platform is undefined");

		try {
			PlatformRepoService.getInstance().updateChain(platform, chain.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
