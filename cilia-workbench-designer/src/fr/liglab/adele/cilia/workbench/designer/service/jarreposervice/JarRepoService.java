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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespace;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Sender;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;

/**
 * JarRepoService.
 * 
 * @author Etienne Gandrille
 */
public class JarRepoService extends AbstractRepoService<CiliaJarFile, CiliaJarModel> implements ErrorsAndWarningsFinder {

	/** Singleton instance */
	private static JarRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;

	/** Jar files extension. */
	private final static String ext = ".jar";

	/** Repository Name */
	private final static String repositoryName = "Jar repo service";

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the instance.
	 */
	public static JarRepoService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JarRepoService();
		return INSTANCE;
	}

	/**
	 * Instantiates a new jar repo service.
	 */
	private JarRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<CiliaJarFile> bundles = new ArrayList<CiliaJarFile>();
		for (File jar : list) {
			String path = jar.getPath();
			bundles.add(new CiliaJarFile(path));
		}

		// Updates model with computed one
		model = bundles;

		// Update content provider
		contentProvider = new JarContentProvider(model);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(null);
	}

	public List<MediatorComponent> getMediators() {
		List<MediatorComponent> retval = new ArrayList<MediatorComponent>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getMediatorComponents());

		return retval;
	}

	public List<Adapter> getAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getAdapters());

		return retval;
	}

	public List<Adapter> getInAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (Adapter a : getAdapters())
			if (a.isInAdapter())
				retval.add(a);

		return retval;
	}

	public List<Adapter> getOutAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (Adapter a : getAdapters())
			if (a.isOutAdapter())
				retval.add(a);

		return retval;
	}

	public List<Scheduler> getSchedulers() {
		List<Scheduler> retval = new ArrayList<Scheduler>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSchedulers());

		return retval;
	}

	public Scheduler getScheduler(NameNamespace nn) {
		for (Scheduler s : getSchedulers())
			if (s.getId().equals(nn))
				return s;
		return null;
	}

	public List<Processor> getProcessors() {
		List<Processor> retval = new ArrayList<Processor>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getProcessors());

		return retval;
	}

	public Processor getProcessor(NameNamespace nn) {
		for (Processor p : getProcessors())
			if (p.getId().equals(nn))
				return p;
		return null;
	}

	public List<Collector> getCollectors() {
		List<Collector> retval = new ArrayList<Collector>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getCollectors());

		return retval;
	}

	public Collector getCollector(NameNamespace nn) {
		for (Collector c : getCollectors())
			if (c.getId().equals(nn))
				return c;
		return null;
	}

	public List<Dispatcher> getDispatchers() {
		List<Dispatcher> retval = new ArrayList<Dispatcher>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getDispatchers());

		return retval;
	}

	public Dispatcher getDispatcher(NameNamespace nn) {
		for (Dispatcher d : getDispatchers())
			if (d.getId().equals(nn))
				return d;
		return null;
	}

	public List<Sender> getSenders() {
		List<Sender> retval = new ArrayList<Sender>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSenders());

		return retval;
	}

	public NameNamespace[] getMediatorsId() {
		List<NameNamespace> retval = new ArrayList<NameNamespace>();
		for (MediatorComponent mc : getMediators())
			retval.add((NameNamespace) mc.getId());

		return retval.toArray(new NameNamespace[0]);
	}

	public NameNamespace[] getAdaptersId() {
		List<NameNamespace> retval = new ArrayList<NameNamespace>();
		for (Adapter a : getAdapters())
			retval.add((NameNamespace) a.getId());

		return retval.toArray(new NameNamespace[0]);
	}

	/**
	 * Gets a mediator with the exact id.
	 * 
	 * @param id
	 * @return the mediator, or null if not found
	 */
	public MediatorComponent getMediator(NameNamespace id) {
		for (MediatorComponent m : getMediators())
			if (m.getId().equals(id))
				return m;

		return null;
	}

	/**
	 * Gets a mediator, by its id, using the chain criteria. If the id given in
	 * parameter has its namespace defined, the functions calls
	 * {@link #getMediator(NameNamespace)}. If the namespace is undefined (null
	 * or empty), the comparison is ONLY based on the name, and the namespace is
	 * IGNORED.
	 * 
	 * @param id
	 * @return the mediator, or null if not found
	 */
	public MediatorComponent getMediatorForChain(NameNamespace id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return getMediator(id);

		for (MediatorComponent m : getMediators())
			if (((NameNamespace) m.getId()).getName().equals(id.getName()))
				return m;

		return null;
	}

	/**
	 * Gets an adaptor with the exact id.
	 * 
	 * @param id
	 * @return the adapter, or null if not found.
	 */
	public Adapter getAdapter(NameNamespace id) {
		for (Adapter a : getAdapters())
			if (a.getId().equals(id))
				return a;

		return null;
	}

	/**
	 * Gets a adapter, by its id, using the chain criteria. If the id given in
	 * parameter has its namespace defined, the functions calls
	 * {@link #getAdapter(NameNamespace)}. If the namespace is undefined (null
	 * or empty), the comparison is ONLY based on the name, and the namespace is
	 * IGNORED.
	 * 
	 * @param id
	 * @return the adapter, or null if not found
	 */
	public Adapter getAdapterForChain(NameNamespace id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return getAdapter(id);

		for (Adapter a : getAdapters())
			if (((NameNamespace) a.getId()).getName().equals(id.getName()))
				return a;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.
	 * AbstractRepoService#getContentForNewFile()
	 */
	@Override
	protected String getContentForNewFile() {
		throw new RuntimeException("File creation is not allowed");
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = new ArrayList<CiliaFlag>();

		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getMediators()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getSchedulers()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getProcessors()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getDispatchers()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getCollectors()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getSenders()));
		errorList.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, getAdapters()));

		for (MediatorComponent mediator : getMediators()) {
			NameNamespace snn = mediator.getSchedulerNameNamespace();
			NameNamespace pnn = mediator.getProcessorNameNamespace();
			NameNamespace dnn = mediator.getDispatcherNameNamespace();

			if (getScheduler(snn) == null)
				errorList.add(new CiliaError("can't find scheduler " + snn, mediator));

			if (getProcessor(pnn) == null)
				errorList.add(new CiliaError("can't find processor " + pnn, mediator));

			if (getDispatcher(dnn) == null)
				errorList.add(new CiliaError("can't find dispatcher " + dnn, mediator));
		}

		return CiliaFlag.generateTab(errorList);
	}
}
