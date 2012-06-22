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
package fr.liglab.adele.cilia.workbench.designer.service.chain.common;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainElement;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.ChainModel;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractFile;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class ChainRepoService<ModelType extends AbstractFile<AbstractType>, AbstractType extends ChainModel<ChainType>, ChainType extends ChainElement<?>>
		extends AbstractRepoService<ModelType, AbstractType> implements ErrorsAndWarningsFinder {

	private final String rootNodeName;

	protected ChainRepoService(String preferenceKey, String ext, String repoName, String rootNodeName) {
		super(preferenceKey, ext, repoName);
		this.rootNodeName = rootNodeName;
	}

	@Override
	protected String getContentForNewFile() {
		return "<" + rootNodeName + ">\n</" + rootNodeName + ">";
	}

	@SuppressWarnings("unchecked")
	protected ModelType getFileObject(ChainType chain) {
		return (ModelType) getContentProvider().getParent(chain);
	}

	protected List<ChainType> getChains() {
		List<ChainType> retval = new ArrayList<ChainType>();
		for (AbstractType model : findAbstractElements())
			retval.addAll(model.getChains());
		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = IdentifiableUtils.getErrorsNonUniqueId(this, getChains());

		return CiliaFlag.generateTab(errorList);
	}

	public ChainType findChain(NameNamespaceID chainName) {
		for (ChainType chain : getChains())
			if (chain.getId().equals(chainName))
				return chain;
		return null;
	}

	protected List<Changeset> merge(List<ModelType> repoElements) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		retval.addAll(MergeUtil.mergeLists(repoElements, model));

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	public String isNewChainNameAllowed(NameNamespaceID id) {

		if (isNameUsesAllowedChar(id.getName()) != null)
			return isNameUsesAllowedChar(id.getName());

		if (isNameUsesAllowedChar(id.getNamespace()) != null)
			return isNameUsesAllowedChar(id.getNamespace());

		if (id.getName().length() == 0) {
			return "Empty name is not allowed";
		}

		ChainType chain = findChain(id);
		if (chain != null) {
			return "A chain with this name/namespace already exists in the repository.";
		}

		return null;
	}

	public void createChain(ChainFile<?, ?> repo, NameNamespaceID id) {
		if (repo.getModel() == null)
			return;
		if (isNewChainNameAllowed(id) != null)
			return;

		try {
			repo.getModel().createChain(id);
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	}
}
