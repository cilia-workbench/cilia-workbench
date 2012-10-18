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
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class CreateFileHandler extends PlatformViewHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		NewPlatformDialog dialog = new NewPlatformDialog(ViewUtil.getShell(event));
		
		// Dialog creation
		if (dialog.open() == Window.OK) {
			String values[] = dialog.getValues();
			
			String name = values[0];
			String host = values[1];
			String port = values[2];
			
			return PlatformRepoService.getInstance().createFile(name, host, port);
		}

		return null;
	}
}