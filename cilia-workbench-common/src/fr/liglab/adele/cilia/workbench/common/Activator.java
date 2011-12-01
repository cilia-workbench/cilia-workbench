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
package fr.liglab.adele.cilia.workbench.common;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	public static final String FILE = "file";
	private static final String FILE_PATH = "icons/16/file.png";
	public static final String FILE_ERROR = "fileError";
	private static final String FILE_ERROR_PATH = "icons/16/fileError.png";
	public static final String CHAIN = "chain";
	private static final String CHAIN_PATH = "icons/16/chain.png";
	public static final String IN_ADAPTER = "in_adapter";
	private static final String IN_ADAPTER_PATH = "icons/16/adapterIn.png";
	public static final String OUT_ADAPTER = "out_adapter";
	private static final String OUT_ADAPTER_PATH = "icons/16/adapterOut.png";
	public static final String REPO = "repo";
	private static final String REPO_PATH = "icons/16/repo.png";
	public static final String COLLECTOR = "collector";
	private static final String COLLECTOR_PATH = "icons/16/collector.png";
	public static final String DISPATCHER = "dispatcher";
	private static final String DISPATCHER_PATH = "icons/16/dispatcher.png";
	public static final String MEDIATOR = "mediator";
	private static final String MEDIATOR_PATH = "icons/16/mediator.png";
	public static final String PROCESSOR = "processor";
	private static final String PROCESSOR_PATH = "icons/16/processor.png";
	public static final String SCHEDULER = "scheduler";
	private static final String SCHEDULER_PATH = "icons/16/scheduler.png";
	public static final String SENDER = "sender";
	private static final String SENDER_PATH = "icons/16/sender.png";
	public static final String IN_PORT = "in_port";
	private static final String IN_PORT_PATH = "icons/16/portIn.png";
	public static final String OUT_PORT = "out_port";
	private static final String OUT_PORT_PATH = "icons/16/portOut.png";

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "fr.liglab.adele.cilia.workbench.common"; //$NON-NLS-1$

	/** The shared instance */
	private static Activator plugin;

	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {

		Bundle bundle = getBundle();
		ImageRegistry registry = getImageRegistry();

		populateRegistry(bundle, registry, FILE, FILE_PATH);
		populateRegistry(bundle, registry, FILE_ERROR, FILE_ERROR_PATH);
		populateRegistry(bundle, registry, CHAIN, CHAIN_PATH);
		populateRegistry(bundle, registry, IN_ADAPTER, IN_ADAPTER_PATH);
		populateRegistry(bundle, registry, OUT_ADAPTER, OUT_ADAPTER_PATH);
		populateRegistry(bundle, registry, REPO, REPO_PATH);
		populateRegistry(bundle, registry, COLLECTOR, COLLECTOR_PATH);
		populateRegistry(bundle, registry, DISPATCHER, DISPATCHER_PATH);
		populateRegistry(bundle, registry, MEDIATOR, MEDIATOR_PATH);
		populateRegistry(bundle, registry, PROCESSOR, PROCESSOR_PATH);
		populateRegistry(bundle, registry, SCHEDULER, SCHEDULER_PATH);
		populateRegistry(bundle, registry, SENDER, SENDER_PATH);
		populateRegistry(bundle, registry, IN_PORT, IN_PORT_PATH);
		populateRegistry(bundle, registry, OUT_PORT, OUT_PORT_PATH);
	}

	private static void populateRegistry(Bundle bundle, ImageRegistry registry, String imageId, String path) {
		ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(path), null));
		registry.put(imageId, myImage);
	}
}
