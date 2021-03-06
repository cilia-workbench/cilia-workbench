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
import org.eclipse.jface.dialogs.MessageDialog;

import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryViewHandler;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class PlatformViewHandler extends RepositoryViewHandler {

	public PlatformViewHandler() {
		super(PlatformView.VIEW_ID);
	}

	protected PlatformChain getPlatformChainOrDisplayErrorDialog(ExecutionEvent event) {
		Object element = getFirstSelectedElementInRepositoryView(event);

		if (element == null || !(element instanceof PlatformChain)) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Please select a platform chain first");
			return null;
		}

		return (PlatformChain) element;
	}

	protected PlatformFile getPlatformFileOrDisplayErrorDialog(ExecutionEvent event) {

		Object element = getFirstSelectedElementInRepositoryView(event);

		if (element == null || !(element instanceof PlatformFile)) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Please select a platform first");
			return null;
		}

		PlatformFile file = (PlatformFile) element;
		PlatformModel model = file.getModel();
		if (model == null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", "Model is in a non valid state");
			return null;
		}

		PlatformID platformID = model.getPlatformID();

		if (platformID.isValid() != null) {
			MessageDialog.openError(ViewUtil.getShell(event), "Error", platformID.isValid());
			return null;
		}

		return file;
	}
}
