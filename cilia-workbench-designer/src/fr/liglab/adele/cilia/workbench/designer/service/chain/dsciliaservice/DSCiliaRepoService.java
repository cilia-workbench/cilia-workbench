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
package fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;

/**
 * A central place for managing the DSCilia repository. The repository can be
 * asked to refresh the model. The repository can be asked to send model update
 * notifications.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaRepoService extends AbstractRepoService<DSCiliaFile, DSCiliaModel> implements
		ErrorsAndWarningsFinder {

	/** Singleton instance */
	private static DSCiliaRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.CONCRETE_COMPO_REPOSITORY_PATH;

	/** files extension. */
	public final static String ext = ".dscilia";

	/** Repository Name */
	private final static String repositoryName = "DSCilia repo service";

	public static DSCiliaRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DSCiliaRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	/**
	 * Constructor. Registers for repository path update and constructs the
	 * model.
	 */
	private DSCiliaRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);
	}

	/**
	 * Updates the model and sends notifications.
	 */
	public void updateModel() {

		File[] list = getFiles();
		List<DSCiliaFile> elements = new ArrayList<DSCiliaFile>();
		for (File file : list) {
			elements.add(new DSCiliaFile(file));
		}

		// Updates existing model with computed model
		List<Changeset> changes = null;
		try {
			changes = merge(elements);
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		// Update content provider
		contentProvider = new DSCiliaContentProvider(model);

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
	private List<Changeset> merge(List<DSCiliaFile> repoElements) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		retval.addAll(MergeUtil.mergeLists(repoElements, this.model));

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	protected String getContentForNewFile() {
		return "<" + DSCiliaModel.ROOT_NODE_NAME + ">\n</" + DSCiliaModel.ROOT_NODE_NAME + ">";
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = IdentifiableUtils.getErrorsNonUniqueId(this, getChains());

		return CiliaFlag.generateTab(errorList);
	}

	private List<Chain> getChains() {
		List<Chain> retval = new ArrayList<Chain>();
		for (DSCiliaModel model : findAbstractElements())
			retval.addAll(model.getChains());
		return retval;
	}
}
