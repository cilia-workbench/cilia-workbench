package fr.liglab.adele.cilia.workbench.monitoring;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	/** Plug-in id */
	public static final String PLUGIN_ID = "fr.liglab.adele.cilia.workbench.monitoring"; //$NON-NLS-1$
	
	/** The Instance itself. */
	private static Activator plugin;
	
	/** The bundle context */
	private static BundleContext context;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/**
	 * Gets the bundle context.
	 *
	 * @return the bundle context
	 */
	public static BundleContext getBundleContext() {
		return context;
	}
	
	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("static-access")
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;
	}

	/**
	 * Retrieve a service in the registry, thanks to a class.
	 *
	 * @param clazz The class used to find the service reference
	 * @return the service reference, null if not found
	 */
	@SuppressWarnings("rawtypes")
	private static ServiceReference retrieveAdminService(Class clazz) {
		ServiceReference[] refs = null;
		ServiceReference refData;
		try {
			refs = context.getServiceReferences(clazz.getName(), null);
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException("Admin data service lookup unrecoverable error", e);
		}
		if (refs != null)
			refData = refs[0];
		else {
			refData = null;
		}
		return refData;
	}

	/**
	 * Gets the application monitored in the service registry.
	 * 
	 * @return the applicationMonitored, or null if not found.
	 */
	public static MonitoredApplication getMonitoredApplication() {
		ServiceReference srvRef = retrieveAdminService(MonitoredApplication.class);
		if (srvRef == null)
			return null;
		Object service = context.getService(srvRef);
		if (service == null)
			return null;
		else
			return (MonitoredApplication) service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}
