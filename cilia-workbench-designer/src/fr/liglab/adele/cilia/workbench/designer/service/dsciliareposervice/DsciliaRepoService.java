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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaModel;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.MergeUtil;

/**
 * A central place for managing the DScilia repository. The repository can be
 * asked to refresh the model. The repository can be asked to send model update
 * notifications.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaRepoService extends AbstractRepoService<DsciliaFile, DsciliaModel> implements
		ErrorsAndWarningsFinder {

	/** Singleton instance */
	private static DsciliaRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH;

	/** DScilia files extension. */
	private final static String ext = ".dscilia";

	/** Repository Name */
	private final static String repositoryName = "DSCilia repo service";

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the instance.
	 */
	public static DsciliaRepoService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DsciliaRepoService();
		return INSTANCE;
	}

	/**
	 * Constructor. Registers for repository path update and constructs the
	 * model.
	 */
	private DsciliaRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);
	}

	/**
	 * Updates the model and sends notifications.
	 */
	public void updateModel() {

		File[] list = getFiles();
		List<DsciliaFile> elements = new ArrayList<DsciliaFile>();
		for (File jar : list) {
			String path = jar.getPath();
			elements.add(new DsciliaFile(path));
		}

		// Updates existing model with computed model
		List<Changeset> changes = null;
		try {
			changes = merge(elements);
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		// Update content provider
		contentProvider = new DsciliaContentProvider(model);

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
	 * @throws CiliaException
	 */
	private List<Changeset> merge(List<DsciliaFile> repoElements) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		retval.addAll(MergeUtil.mergeLists(repoElements, this.model));

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
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
		return "<" + DsciliaModel.ROOT_NODE_NAME + ">\n</" + DsciliaModel.ROOT_NODE_NAME + ">";
	}

	public String isNewChainNameAllowed(String chainName) {

		if (isNameUsesAllowedChar(chainName) != null)
			return isNameUsesAllowedChar(chainName);

		if (chainName.length() == 0) {
			return "Empty name is not allowed";
		}

		Chain chain = findChain(chainName);
		if (chain != null) {
			return "A chain with this name already exists in the repository.";
		}

		return null;
	}

	private Chain findChain(String chainName) {
		for (Chain chain : getChains())
			if (chain.getId().equalsIgnoreCase(chainName))
				return chain;
		return null;
	}

	private List<Chain> getChains() {
		List<Chain> retval = new ArrayList<Chain>();
		for (DsciliaModel model : findAbstractElements())
			retval.addAll(model.getChains());
		return retval;
	}

	/**
	 * Creates the chain in a repository element.
	 * 
	 * @param repo
	 *            the repo
	 * @param chainName
	 *            the chain name
	 */
	public void createChain(DsciliaFile repo, String chainName) {
		if (repo.getModel() == null)
			return;
		if (isNewChainNameAllowed(chainName) != null)
			return;

		try {
			repo.getModel().createChain(chainName);
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}

	public void deleteChain(Chain chain) {
		DsciliaFile repo = (DsciliaFile) contentProvider.getParent(chain);
		if (repo == null)
			return;
		try {
			repo.getModel().deleteChain(chain.getId());
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}

	public DsciliaFile getRepoElement(Object object) {
		Preconditions.checkNotNull(object);

		if (object instanceof DsciliaFile)
			return (DsciliaFile) object;
		Object parent = getContentProvider().getParent(object);
		if (parent != null)
			return getRepoElement(parent);
		else
			return null;
	}

	public void createMediatorInstance(Chain chain, String id, NameNamespaceID type) throws CiliaException {
		DsciliaFile repo = (DsciliaFile) getContentProvider().getParent(chain);
		if (repo == null)
			return;
		repo.getModel().createMediatorInstance(chain, id, type);
	}

	public void createAdapterInstance(Chain chain, String id, NameNamespaceID type) throws CiliaException {
		DsciliaFile repo = (DsciliaFile) getContentProvider().getParent(chain);
		if (repo == null)
			return;
		repo.getModel().createAdapterInstance(chain, id, type);
	}

	public void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort)
			throws CiliaException {
		DsciliaFile repo = (DsciliaFile) getContentProvider().getParent(chain);
		if (repo == null)
			return;
		repo.getModel().createBinding(chain, srcElem, srcPort, dstElem, dstPort);
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = IdentifiableUtils.getErrorsNonUniqueId(this, getChains());

		return CiliaFlag.generateTab(errorList);
	}
}
