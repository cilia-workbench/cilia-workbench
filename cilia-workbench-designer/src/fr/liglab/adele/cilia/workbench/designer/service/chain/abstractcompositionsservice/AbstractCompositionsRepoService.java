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
package fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.designer.misc.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractCompositionModel;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.MediatorSpecRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ComponentRef;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.MediatorRef;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.Cardinality;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IGenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.service.chain.common.ChainRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;

/**
 * A central place for managing the abstract composition repository. The
 * repository can be asked to refresh the model. The repository can be asked to
 * send model update notifications.
 * 
 * @author Etienne Gandrille
 */
public class AbstractCompositionsRepoService extends
		ChainRepoService<AbstractCompositionFile, AbstractCompositionModel, AbstractChain> {

	// super type parameters
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.ABSTRACT_COMPO_REPOSITORY_PATH;
	public final static String ext = ".compo";
	private final static String repositoryName = "Abstract compositions repo service";

	// Singleton instance
	private static AbstractCompositionsRepoService INSTANCE;

	public static AbstractCompositionsRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AbstractCompositionsRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	private AbstractCompositionsRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName, AbstractCompositionModel.ROOT_NODE_NAME);
	}

	/**
	 * Updates the model and sends notifications.
	 */
	public void updateModel() {

		File[] list = getFiles();
		List<AbstractCompositionFile> elements = new ArrayList<AbstractCompositionFile>();
		for (File jar : list) {
			elements.add(new AbstractCompositionFile(jar));
		}

		// Updates existing model with computed model
		List<Changeset> changes = null;
		try {
			changes = merge(elements);
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		// Update content provider
		contentProvider = new AbstractCompositionsContentProvider(model);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(changes);
	}

	public void deleteChain(AbstractChain chain) {
		if (getFileObject(chain) == null)
			return;
		try {
			getFileObject(chain).getModel().deleteChain(chain.getId());
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}

	public void createMediator(AbstractChain chain, String id, IGenericMediator type) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().createMediator(chain, id, type);
	}

	public void createAdapter(AbstractChain chain, String id, IGenericAdapter type) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().createAdapter(chain, id, type);
	}

	public void deleteComponent(AbstractChain chain, ComponentRef<AbstractChain> component) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().deleteComponent(chain, component);
	}

	public void createBinding(AbstractChain chain, String srcElem, String srcPort, String dstElem, String dstPort,
			Cardinality srcCard, Cardinality dstCard) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().createBinding(chain, srcElem, srcPort, dstElem, dstPort, srcCard, dstCard);
	}

	public void deleteBinding(AbstractChain chain, Binding binding) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().deleteBinding(chain, binding);
	}

	public void updateProperties(AbstractChain chain, MediatorSpecRef<AbstractChain> mediator,
			Map<String, String> properties) throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().updateProperties(chain, mediator, properties);
	}

	public void updateParameters(AbstractChain chain, MediatorRef<AbstractChain> mediator,
			Map<String, String> schedulerParam, Map<String, String> processorParam, Map<String, String> dispatcherParam)
			throws CiliaException {
		if (getFileObject(chain) == null)
			return;
		getFileObject(chain).getModel().updateParameters(chain, mediator, schedulerParam, processorParam,
				dispatcherParam);
	}

	public AbstractCompositionFile getRepoElement(Object object) {
		return (AbstractCompositionFile) getContentProvider().getAncestor(object, AbstractCompositionFile.class);
	}
}