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
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.IAdapter;
import fr.liglab.adele.cilia.workbench.common.parser.element.IMediator;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLBinding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;

/**
 * A central place for managing the DSCilia repository. The repository can be
 * asked to refresh the model. The repository can be asked to send model update
 * notifications.
 * 
 * @author Etienne Gandrille
 */
public class DSCiliaRepoService extends ChainRepoService<DSCiliaFile, DSCiliaModel, DSCiliaChain> {

	// Super type parameters
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.CONCRETE_COMPO_REPOSITORY_PATH;
	public final static String ext = ".dscilia";
	private final static String repositoryName = "DSCilia repo service";

	// Singleton instance
	private static DSCiliaRepoService INSTANCE;

	public static DSCiliaRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DSCiliaRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	private DSCiliaRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName, DSCiliaModel.ROOT_NODE_NAME);
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
		contentProvider = new DSCiliaContentProvider(repoContent);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(changes);
	}

	public void createMediator(DSCiliaChain chain, String id, IMediator type) throws CiliaException {
		if (getFileFromChain(chain) == null)
			return;
		getFileFromChain(chain).getModel().createMediator(chain, id, type);
	}

	public void createAdapter(DSCiliaChain chain, String id, IAdapter type) throws CiliaException {
		if (getFileFromChain(chain) == null)
			return;
		getFileFromChain(chain).getModel().createAdapter(chain, id, type);
	}

	public void deleteComponent(DSCiliaChain chain, ComponentRef component) throws CiliaException {
		if (getFileFromChain(chain) == null)
			return;
		getFileFromChain(chain).getModel().deleteComponent(chain, component);
	}

	public void createBinding(DSCiliaChain chain, String srcElem, String srcPort, String dstElem, String dstPort) throws CiliaException {
		if (getFileFromChain(chain) == null)
			return;
		getFileFromChain(chain).getModel().createBinding(chain, srcElem, srcPort, dstElem, dstPort, null, null);
	}

	public void deleteBinding(DSCiliaChain chain, XMLBinding binding) throws CiliaException {
		if (getFileFromChain(chain) == null)
			return;
		getFileFromChain(chain).getModel().deleteBinding(chain, binding);
	}
}
