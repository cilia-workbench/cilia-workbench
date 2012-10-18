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


import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.TextsDialog;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * Dialog used to create a new platform. Asks for 
 * <ul>
 * <li>name</li>
 * <li>host</li>
 * <li>port</li>
 * </ul>
 * @author Etienne Gandrille
 */
public class NewPlatformDialog extends TextsDialog {

	private final static String title = "New Platform";
	private final static Point initialSize = new Point(400, 200);
	private final static boolean isOkButtonEnable = false;
	private final static String labels[] = {"name", "host", "port"};
	private final static TextsDialogValidator validator = new NewPlatformValidator();
		
	NewPlatformDialog(Shell parentShell) {
		super(parentShell, title, initialSize, isOkButtonEnable, labels, validator);
	}

	private static class NewPlatformValidator implements TextsDialogValidator {
		@Override
		public String validate(ModifyEvent event, String[] values) {
			String msg = null;
			
			// File Name validation
			msg = PlatformRepoService.getInstance().isNewFileNameAllowed(values[0]);
			if (msg != null)
				return msg;
						
			// host validation
			msg = PlatformModel.hostValidator(values[1]);
			if (msg != null)
				return msg;
			
			// Port validator
			msg = PlatformModel.portValidator(values[2]);
			if (msg != null)
				return msg;

			return null;
		}
	}
}
