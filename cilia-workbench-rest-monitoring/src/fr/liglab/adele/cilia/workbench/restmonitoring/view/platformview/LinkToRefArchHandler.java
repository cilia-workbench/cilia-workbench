/**
 * Copyright 2012-2013 France Télécom 
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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.SimpleListDialog;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryViewHandler;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractCompositionFile;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class LinkToRefArchHandler extends RepositoryViewHandler {

	public LinkToRefArchHandler() {
		super(PlatformView.VIEW_ID);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Map<String, AbstractChain> chainNamesMap = new HashMap<String, AbstractChain>();

		List<AbstractCompositionFile> files = AbstractCompositionsRepoService.getInstance().getRepoContent();
		for (AbstractCompositionFile file : files) {
			if (file.getModel() != null) {
				for (AbstractChain chain : file.getModel().getChains()) {
					NameNamespaceID chainId = chain.getId();
					if (chainId != null)
						chainNamesMap.put(chainId.getName() + " (" + file.toString() + ")", chain);
				}
			}
		}

		Set<String> chainNames = chainNamesMap.keySet();

		SimpleListDialog dialog = new SimpleListDialog(ViewUtil.getShell(event), "title", "message", chainNames);
		dialog.open();

		return null;
	}
}
