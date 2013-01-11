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
package fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.misc.FileUtil;
import fr.liglab.adele.cilia.workbench.common.parser.MetadataInJar;
import fr.liglab.adele.cilia.workbench.common.parser.MetadataInJarInDP;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter.AdapterType;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.ComponentRepoService;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.CollectorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.DispatcherImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.ProcessorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SchedulerImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.SenderImplem;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class CiliaJarRepoService extends ComponentRepoService<CiliaJarFile, CiliaJarModel> implements ErrorsAndWarningsFinder, IRepoServiceListener {

	/** Singleton instance */
	private static CiliaJarRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;

	/** Jar files extension. */
	private final static String ext = ".jar";

	/** Repository Name */
	private final static String repositoryName = "Jar repo service";

	public static CiliaJarRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CiliaJarRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	private CiliaJarRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);

		SpecRepoService.getInstance().registerListener(this);
	}

	private List<JarEntry> findJarInDP(File jarFile) {
		List<JarEntry> retval = new ArrayList<JarEntry>();

		JarFile file;
		try {
			file = new JarFile(jarFile);
			Enumeration<JarEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith("bundles/") && entry.getName().endsWith(".jar")) {
					retval.add(entry);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retval;
	}

	public void updateModel() {
		File[] list = getFiles();
		List<CiliaJarFile> bundles = new ArrayList<CiliaJarFile>();
		for (File jar : list) {
			MetadataInJar mij = new MetadataInJar(jar);
			if (mij.hasMetadata())
				bundles.add(new CiliaJarFile(mij));
		}

		// Deployment packages management
		for (File jarFile : FileUtil.getFiles(getRepositoryLocation(), "dp")) {
			for (JarEntry entry : findJarInDP(jarFile)) {
				MetadataInJarInDP mij = new MetadataInJarInDP(jarFile, entry.getName());
				if (mij.hasMetadata())
					bundles.add(new CiliaJarFile(mij));
			}
		}

		// Updates model with computed one
		repoContent = bundles;

		// Update content provider
		contentProvider = new JarContentProvider(repoContent);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(null);
	}

	public List<MediatorImplem> getMediators() {
		List<MediatorImplem> retval = new ArrayList<MediatorImplem>();
		for (CiliaJarFile bundle : repoContent)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getMediatorComponents());

		return retval;
	}

	public List<Adapter> getAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (CiliaJarFile bundle : repoContent)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getAdapters());

		return retval;
	}

	public List<Adapter> getInAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (Adapter a : getAdapters())
			if (a.getType() == AdapterType.IN)
				retval.add(a);

		return retval;
	}

	public List<Adapter> getOutAdapters() {
		List<Adapter> retval = new ArrayList<Adapter>();
		for (Adapter a : getAdapters())
			if (a.getType() == AdapterType.OUT)
				retval.add(a);

		return retval;
	}

	public static List<SchedulerImplem> getSchedulers(List<CiliaJarFile> model) {
		List<SchedulerImplem> retval = new ArrayList<SchedulerImplem>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSchedulers());

		return retval;
	}

	public static SchedulerImplem getScheduler(List<CiliaJarFile> model, NameNamespaceID nn) {
		for (SchedulerImplem s : getSchedulers(model))
			if (s.getId().equals(nn))
				return s;
		return null;
	}

	public List<SchedulerImplem> getSchedulers() {
		return getSchedulers(repoContent);
	}

	public SchedulerImplem getScheduler(NameNamespaceID nn) {
		return getScheduler(repoContent, nn);
	}

	public static List<ProcessorImplem> getProcessors(List<CiliaJarFile> model) {
		List<ProcessorImplem> retval = new ArrayList<ProcessorImplem>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getProcessors());

		return retval;
	}

	public static ProcessorImplem getProcessor(List<CiliaJarFile> model, NameNamespaceID nn) {
		for (ProcessorImplem p : getProcessors(model))
			if (p.getId().equals(nn))
				return p;
		return null;
	}

	public List<ProcessorImplem> getProcessors() {
		return getProcessors(repoContent);
	}

	public ProcessorImplem getProcessor(NameNamespaceID nn) {
		return getProcessor(repoContent, nn);
	}

	public static List<DispatcherImplem> getDispatchers(List<CiliaJarFile> model) {
		List<DispatcherImplem> retval = new ArrayList<DispatcherImplem>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getDispatchers());

		return retval;
	}

	public static DispatcherImplem getDispatcher(List<CiliaJarFile> model, NameNamespaceID nn) {
		for (DispatcherImplem d : getDispatchers(model))
			if (d.getId().equals(nn))
				return d;
		return null;
	}

	public List<DispatcherImplem> getDispatchers() {
		return getDispatchers(repoContent);
	}

	public DispatcherImplem getDispatcher(NameNamespaceID nn) {
		return getDispatcher(repoContent, nn);
	}

	public List<CollectorImplem> getCollectors() {
		List<CollectorImplem> retval = new ArrayList<CollectorImplem>();
		for (CiliaJarFile bundle : repoContent)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getCollectors());

		return retval;
	}

	public CollectorImplem getCollector(String name) {
		for (CollectorImplem c : getCollectors())
			if (c.getId().equals(name))
				return c;
		return null;
	}

	public List<SenderImplem> getSenders() {
		List<SenderImplem> retval = new ArrayList<SenderImplem>();
		for (CiliaJarFile bundle : repoContent)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSenders());

		return retval;
	}

	public SenderImplem getSender(String name) {
		for (SenderImplem s : getSenders())
			if (s.getId().equals(name))
				return s;
		return null;
	}

	public NameNamespaceID[] getMediatorsId() {
		List<NameNamespaceID> retval = new ArrayList<NameNamespaceID>();
		for (MediatorImplem mc : getMediators())
			retval.add((NameNamespaceID) mc.getId());

		return retval.toArray(new NameNamespaceID[0]);
	}

	public NameNamespaceID[] getAdaptersId() {
		List<NameNamespaceID> retval = new ArrayList<NameNamespaceID>();
		for (Adapter a : getAdapters())
			retval.add((NameNamespaceID) a.getId());

		return retval.toArray(new NameNamespaceID[0]);
	}

	/**
	 * Gets a mediator with the exact id.
	 * 
	 * @param id
	 * @return the mediator, or null if not found
	 */
	public MediatorImplem getMediator(NameNamespaceID id) {
		for (MediatorImplem m : getMediators())
			if (m.getId().equals(id))
				return m;

		return null;
	}

	/**
	 * Gets a mediator, by its id, using the chain criteria. If the id given in
	 * parameter has its namespace defined, the functions calls
	 * {@link #getMediator(NameNamespaceID)}. If the namespace is undefined
	 * (null or empty), the comparison is ONLY based on the name, and the
	 * namespace is IGNORED.
	 * 
	 * @param id
	 * @return the mediator, or null if not found
	 */
	public MediatorImplem getMediatorForChain(NameNamespaceID id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return getMediator(id);

		for (MediatorImplem m : getMediators())
			if (((NameNamespaceID) m.getId()).getName().equals(id.getName()))
				return m;

		return null;
	}

	/**
	 * Gets an adapter with the exact id.
	 * 
	 * @param id
	 * @return the adapter, or null if not found.
	 */
	public Adapter getAdapter(NameNamespaceID id) {
		for (Adapter a : getAdapters())
			if (a.getId().equals(id))
				return a;

		return null;
	}

	/**
	 * Gets a adapter, by its id, using the chain criteria. If the id given in
	 * parameter has its namespace defined, the functions calls
	 * {@link #getAdapter(NameNamespaceID)}. If the namespace is undefined (null
	 * or empty), the comparison is ONLY based on the name, and the namespace is
	 * IGNORED.
	 * 
	 * @param id
	 * @return the adapter, or null if not found
	 */
	public Adapter getAdapterForChain(NameNamespaceID id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return getAdapter(id);

		for (Adapter a : getAdapters())
			if (((NameNamespaceID) a.getId()).getName().equals(id.getName()))
				return a;

		return null;
	}

	@Override
	protected String getContentForNewFile(String... parameters) {
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

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		updateModel();
	}
}
