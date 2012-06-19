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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.abstractchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.TextListDialog;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IGenericAdapter;
import fr.liglab.adele.cilia.workbench.designer.service.jarreposervice.JarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewAdapterDialog extends TextListDialog {

	private final Chain chain;

	protected NewAdapterDialog(Shell parentShell, Chain chain) {
		super(parentShell, "New Adapter", "id", "type", getListValues());
		this.chain = chain;
	}

	private static Map<String, Object> getListValues() {
		Map<String, Object> retval = new HashMap<String, Object>();

		List<IGenericAdapter> list = new ArrayList<IGenericAdapter>();
		list.addAll(JarRepoService.getInstance().getAdapters());
		// list.addAll(SpecRepoService.getInstance().getAdapterSpecs());

		for (IGenericAdapter a : list) {
			NameNamespaceID id = a.getId();
			String str = a.getNature().getShortName() + " ";
			String key = str + id.getName();
			if (!Strings.isNullOrEmpty(id.getNamespace()))
				key = key + " (" + id.getNamespace() + ")";
			retval.put(key, a);
		}

		return retval;
	}

	@Override
	protected String checkValidValues(String id, Object object) {
		if (object == null)
			return "Please select an element in the combo";
		IGenericAdapter a = (IGenericAdapter) object;
		return chain.isNewComponentAllowed(id, a.getId());
	}
}
