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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.akquinet.gomobile.deployment.api.DeploymentPackage;
import de.akquinet.gomobile.deployment.api.Resource;
import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.files.StreamUtil;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.SimpleListDialog;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.http.CiliaRestHelper;

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

		// remove dp files from dp
		List<File> fileResources = new ArrayList<File>();
		for (String resource : resources)
			if (!resource.endsWith(".dp"))
				fileResources.add(new File(resource));

		// dp creation
		DeploymentPackage dp = null;
		try {
			dp = createDP("dp-" + System.currentTimeMillis(), "1.0.0", fileResources);
		} catch (IOException e) {
			MessageDialog.openError(parentShell, "Error", "Error while creating deployment package");
			e.printStackTrace();
			return null;
		}

		// building dp
		InputStream is = null;
		try {
			// dp.build(new File("/home/etienne/test.dp"));
			is = dp.build();
		} catch (Exception e) {
			MessageDialog.openError(parentShell, "Error", "Error while building deployment package");
			e.printStackTrace();
			return null;
		}

		// Sending dp using rest API
		try {
			CiliaRestHelper.sendDPtoPlatform(pfFile.getModel().getPlatformID(), is);
		} catch (CiliaException e) {
			MessageDialog.openError(parentShell, "Error", "Error while sending HTTP request to platform:\n" + e.getMessage());
			e.printStackTrace();
			StreamUtil.closeStream(is);
			return null;
		}

		// Success message
		String pfName = pfFile.getModel().getPlatformID().toString();
		StringBuilder sb = new StringBuilder();
		for (File res : fileResources) {
			sb.append(" * ");
			sb.append(res.getName());
			sb.append("\n");
		}
		String msg = "Artifacts deployed to " + pfName + "\n" + sb.toString();
		MessageDialog.openInformation(parentShell, "Success", msg);

		StreamUtil.closeStream(is);

		return null;
	}

	private DeploymentPackage createDP(String name, String version, List<File> resources) throws IOException {
		DeploymentPackage dp = new DeploymentPackage();
		dp.setName(name);
		dp.setVersion(version);
		dp.setSymbolicName(name);

		for (File resource : resources) {
			URL url = new URL("file:" + resource);
			String resourceName = resource.getName();

			if (resourceName.endsWith(".jar")) {
				dp.addBundle(url, "bundles/" + resourceName);
			} else {
				Resource res = new Resource();
				res.setPath(resourceName);
				res.setURL(url);
				res.setProcessor("org.osgi.deployment.rp.autoload");
				dp.addResource(res);
			}
		}

		return dp;
	}
}
