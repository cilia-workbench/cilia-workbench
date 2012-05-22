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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Strings;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.view.NewIdListDialog;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IMediator;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * NewMediatorInstanceWindow.
 * 
 * @author Etienne Gandrille
 */
public class NewMediatorWindow extends NewIdListDialog {

	private final Chain chain;

	protected NewMediatorWindow(Shell parentShell, Chain chain) {
		super(parentShell, "New Mediator", "id", "type", getListValues());
		this.chain = chain;
	}

	private static Map<String, Object> getListValues() {
		Map<String, Object> retval = new HashMap<String, Object>();

		List<IMediator> list = new ArrayList<IMediator>();
		list.addAll(JarRepoService.getInstance().getMediators());
		list.addAll(SpecRepoService.getInstance().getMediatorSpecs());

		for (IMediator m : list) {
			NameNamespaceID id = m.getId();
			String str = m.getNature().getShortName() + " ";
			String key = str + id.getName();
			if (!Strings.isNullOrEmpty(id.getNamespace()))
				key = key + " (" + id.getNamespace() + ")";
			retval.put(key, m);
		}

		return retval;
	}

	@Override
	protected String checkValidValues(String id, Object object) {
		if (object == null)
			return "Please select an element in the combo";
		IMediator m = (IMediator) object;
		return chain.isNewComponentAllowed(id, m.getId());
	}
}
