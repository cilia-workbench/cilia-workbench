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
package fr.liglab.adele.cilia.workbench.designer.service.element.specreposervice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.SpecModel;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public class SpecRepoService extends AbstractRepoService<SpecFile, SpecModel> implements ErrorsAndWarningsFinder {

	/** Singleton instance */
	private static SpecRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.SPEC_REPOSITORY_PATH;

	/** Spec files extension. */
	private final static String ext = ".xml";

	/** Repository Name */
	private final static String repositoryName = "Spec repo service";

	public static SpecRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SpecRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	public List<MediatorSpec> getMediatorSpecs() {
		List<MediatorSpec> retval = new ArrayList<MediatorSpec>();

		for (SpecFile spec : model) {
			SpecModel abstractModel = spec.getModel();
			if (abstractModel != null)
				retval.addAll(abstractModel.getMediatorSpecs());
		}
		return retval;
	}

	public MediatorSpec getMediatorSpec(NameNamespaceID id) {

		for (MediatorSpec spec : getMediatorSpecs())
			if (spec.getId().equals(id))
				return spec;

		return null;
	}

	private SpecRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<SpecFile> elements = new ArrayList<SpecFile>();
		for (File file : list) {
			elements.add(new SpecFile(file));
		}

		// Updates existing model with computed model
		List<Changeset> changes = null;
		try {
			changes = merge(elements);
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		// Update content provider
		contentProvider = new SpecContentProvider(model);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(changes);
	}

	/**
	 * Merge a list of repo element into the current model. Only differences
	 * between the argument and the model are merge back into the model.
	 * 
	 * @param repoElements
	 *            a new model
	 * @return a list of changesets, which can be empty.
	 */
	private List<Changeset> merge(List<SpecFile> repoElements) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		retval.addAll(MergeUtil.mergeLists(repoElements, this.model));

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	protected String getContentForNewFile() {
		return "<" + SpecModel.XML_NODE_NAME + ">\n</" + SpecModel.XML_NODE_NAME + ">";
	}

	public void deleteMediatorSpec(MediatorSpec mediator) {
		SpecFile file = (SpecFile) contentProvider.getParent(mediator);
		if (file == null)
			return;
		try {
			file.getModel().deleteMediatorSpec((NameNamespaceID) mediator.getId());
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}

	public void updateMediatorSpec(MediatorSpec mediator, List<String> inPorts, List<String> outPorts,
			List<String> mediatorProperties, List<String> schedulerParam, List<String> processorParam,
			List<String> dispatcherParam) {
		SpecFile file = (SpecFile) contentProvider.getParent(mediator);
		if (file == null)
			return;
		try {
			file.getModel().updateMediatorSpec((NameNamespaceID) mediator.getId(), inPorts, outPorts,
					mediatorProperties, schedulerParam, processorParam, dispatcherParam);
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}

	public String isNewMediatorSpecAllowed(NameNamespaceID id) {
		if (id == null || id.getName() == null || id.getName().isEmpty())
			return "id can't be null or empty";

		if (id.getNamespace() == null || id.getNamespace().isEmpty())
			return "namespace can't be null or empty";

		MediatorSpec spec = findMediatorSpec(id);

		if (spec != null)
			return "a mediator with the same id/namespace already exists";

		return null;
	}

	/**
	 * Finds a mediator spec with a given namespace and id.
	 * 
	 * @param id
	 *            the id
	 * @param namespace
	 *            the namespace
	 * @return the mediator spec
	 */
	private MediatorSpec findMediatorSpec(NameNamespaceID id) {
		for (SpecModel spec : findAbstractElements()) {
			for (MediatorSpec s : spec.getMediatorSpecs())
				if (s.getId().equals(id))
					return s;
		}
		return null;
	}

	public Object createMediatorSpec(SpecModel specModel, NameNamespaceID id) {
		try {
			specModel.createMediatorSpec(id);
		} catch (CiliaException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = IdentifiableUtils.getErrorsNonUniqueId(this, getMediatorSpecs());

		return CiliaFlag.generateTab(errorList);
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
	public MediatorSpec getMediatorForChain(NameNamespaceID id) {
		if (id.getNamespace() != null && id.getNamespace().length() != 0)
			return findMediatorSpec(id);

		for (MediatorSpec spec : getMediatorSpecs())
			if (spec.getId().getName().equals(id.getName()))
				return spec;

		return null;
	}
}
