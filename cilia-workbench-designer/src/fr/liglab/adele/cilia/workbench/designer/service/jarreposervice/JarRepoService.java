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

	public List<Processor> getProcessors() {
		List<Processor> retval = new ArrayList<Processor>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getProcessors());

		return retval;
	}

	public List<Dispatcher> getDispatchers() {
		List<Dispatcher> retval = new ArrayList<Dispatcher>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getDispatchers());

		return retval;
	}

	public List<Collector> getCollectors() {
		List<Collector> retval = new ArrayList<Collector>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getCollectors());

		return retval;
	}

	public List<Sender> getSenders() {
		List<Sender> retval = new ArrayList<Sender>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSenders());

		return retval;
	}

	public String[] getMediatorsId() {
		List<String> retval = new ArrayList<String>();
		for (MediatorComponent mc : getMediators())
			retval.add((String) mc.getId());

		return retval.toArray(new String[0]);
	}

	public String[] getAdaptersId() {
		List<String> retval = new ArrayList<String>();
		for (Adapter a : getAdapters())
			retval.add((String) a.getId());

		return retval.toArray(new String[0]);
	}

	public MediatorComponent getMediator(String id) {
		for (MediatorComponent m : getMediators())
			if (m.getId().equals(id))
				return m;

		return null;
	}

	public Adapter getAdapter(String id) {
		for (Adapter a : getAdapters())
			if (a.getId().equals(id))
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

		return CiliaFlag.generateTab(errorList);
	}
}
