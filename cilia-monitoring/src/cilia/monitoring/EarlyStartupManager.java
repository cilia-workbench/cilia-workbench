package cilia.monitoring;

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
	 * @param symbolicName
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
