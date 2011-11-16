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
package fr.liglab.adele.cilia.workbench.monitoring;

import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;


/**
 * Class referenced in <code>org.eclipse.ui.startup</code> extension point.
 */
public class EarlyStartupManager implements IStartup {

	/**
	 * Bundles symbolic names to be started when plug-in is loading.
	 */
	private final String bundles[] = { "org.apache.felix.ipojo", "org.apache.felix.fileinstall", "cilia-core",
			"cilia-ipojo-runtime", "cilia-ipojo-compendium", "cilia.monitoring", "cilia-chain-parser",
			"cilia-compendium-mediators", "cilia-deployer", "cilia-runtime-management", "cilia-admin-chain",
			"slf4j.api", "slf4j.simple", "org.eclipse.equinox.http.jetty" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
		for (String bundleName : bundles) {
			try {
				Bundle bundleObject = findBundle(bundleName);
				if (bundleObject == null)
					System.out.println(bundleName + " is null");
				else {
					bundleObject.start();
				}
			}
			catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Finds a bundle using its symbolic name.
	 *
	 * @param symbolicName the symbolic name
	 * @return the bundle, null if not found.
	 */
	private Bundle findBundle(String symbolicName) {
		for (Bundle bundle : Activator.getBundleContext().getBundles()) {
			if (bundle.getSymbolicName().equals(symbolicName))
				return bundle;
		}
		return null;
	}
}
