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
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.AdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.DispatcherImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.ProcessorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.SchedulerImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.ciliajar.Sender;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class JarRepoService extends AbstractRepoService<CiliaJarFile, CiliaJarModel> implements
		ErrorsAndWarningsFinder, IRepoServiceListener {

	/** Singleton instance */
	private static JarRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;

	/** Jar files extension. */
	private final static String ext = ".jar";

	/** Repository Name */
	private final static String repositoryName = "Jar repo service";

	public static JarRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JarRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	private JarRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);

		SpecRepoService.getInstance().registerListener(this);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<CiliaJarFile> bundles = new ArrayList<CiliaJarFile>();
		for (File jar : list) {
			bundles.add(new CiliaJarFile(jar));
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

	public List<MediatorImplem> getMediators() {
		List<MediatorImplem> retval = new ArrayList<MediatorImplem>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getMediatorComponents());

		return retval;
	}

	public List<AdapterImplem> getAdapters() {
		List<AdapterImplem> retval = new ArrayList<AdapterImplem>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getAdapters());

		return retval;
	}

	public List<AdapterImplem> getInAdapters() {
		List<AdapterImplem> retval = new ArrayList<AdapterImplem>();
		for (AdapterImplem a : getAdapters())
			if (a.getType() == AdapterType.IN)
				retval.add(a);

		return retval;
	}

	public List<AdapterImplem> getOutAdapters() {
		List<AdapterImplem> retval = new ArrayList<AdapterImplem>();
		for (AdapterImplem a : getAdapters())
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
		return getSchedulers(model);
	}

	public SchedulerImplem getScheduler(NameNamespaceID nn) {
		return getScheduler(model, nn);
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
		return getProcessors(model);
	}

	public ProcessorImplem getProcessor(NameNamespaceID nn) {
		return getProcessor(model, nn);
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
		return getDispatchers(model);
	}

	public DispatcherImplem getDispatcher(NameNamespaceID nn) {
		return getDispatcher(model, nn);
	}

	public List<Collector> getCollectors() {
		List<Collector> retval = new ArrayList<Collector>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getCollectors());

		return retval;
	}

	public Collector getCollector(NameNamespaceID nn) {
		for (Collector c : getCollectors())
			if (c.getId().equals(nn))
				return c;
		return null;
	}

	public List<Sender> getSenders() {
		List<Sender> retval = new ArrayList<Sender>();
		for (CiliaJarFile bundle : model)
			if (bundle.getModel() != null)
				retval.addAll(bundle.getModel().getSenders());

		return retval;
	}

	public NameNamespaceID[] getMediatorsId() {
		List<NameNamespaceID> retval = new ArrayList<NameNamespaceID>();
		for (MediatorImplem mc : getMediators())
			retval.add((NameNamespaceID) mc.getId());

		return retval.toArray(new NameNamespaceID[0]);
	}

	public NameNamespaceID[] getAdaptersId() {
		List<NameNamespaceID> retval = new ArrayList<NameNamespaceID>();
		for (AdapterImplem a : getAdapters())
			retval.add(a.getId());

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
	 * Gets an adaptor with the exact id.
	 * 
	 * @param id
	 * @return the adapter, or null if not found.
	 */
	public AdapterImplem getAdapter(NameNamespaceID id) {
		for (AdapterImplem a : getAdapters())
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
	public AdapterImplem getAdapterForChain(NameNamespaceID id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return getAdapter(id);

		for (AdapterImplem a : getAdapters())
			if (a.getId().getName().equals(id.getName()))
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
