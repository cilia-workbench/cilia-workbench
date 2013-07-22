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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.SimpleListDialog;
import fr.liglab.adele.cilia.workbench.common.ui.view.ViewUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class LinkToRefArchHandler extends PlatformViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		PlatformChain platformChain = getPlatformChainOrDisplayErrorDialog(event);
		if (platformChain == null)
			return null;

		Map<String, AbstractChain> chainNamesMap = new HashMap<String, AbstractChain>();
		List<AbstractChain> chains = AbstractCompositionsRepoService.getInstance().getChains();
		for (AbstractChain chain : chains) {
			NameNamespaceID chainId = chain.getId();
			if (chainId != null)
				chainNamesMap.put(chainId.getName() + " (" + chainId.getNamespace() + ")", chain);
		}

		List<String> chainNames = new ArrayList<String>(chainNamesMap.keySet());
		Collections.sort(chainNames);
		chainNames.add(0, "<< NONE >>");
		chainNamesMap.put("<< NONE >>", null);

		String title = "Link to reference architecture";
		String msg = "Please select a reference architecture";
		SimpleListDialog dialog = new SimpleListDialog(ViewUtil.getShell(event), title, msg, chainNames);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			AbstractChain selection;
			if (result.length == 0)
				selection = null;
			else {
				String selectedLine = (String) result[0];
				if (Strings.isNullOrEmpty(selectedLine))
					selection = null;
				else {
					selection = chainNamesMap.get(selectedLine);
				}
			}

			if (selection != null)
				platformChain.setRefArchitectureID(selection.getId());
			else
				platformChain.setRefArchitectureID(null);
		}

		return null;
	}
}
