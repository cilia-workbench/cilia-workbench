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
package fr.liglab.adele.cilia.workbench.designer;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fr.liglab.adele.cilia.workbench.designer.service.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "fr.liglab.adele.cilia.workbench.designer"; //$NON-NLS-1$

	private static Activator plugin;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// loads repositories, for displaying markers
		SpecRepoService.getInstance();
		JarRepoService.getInstance();
		AbstractCompositionsRepoService.getInstance();
		DSCiliaRepoService.getInstance();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getInstance() {
		return plugin;
	}
}
