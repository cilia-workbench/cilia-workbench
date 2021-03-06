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
package fr.liglab.adele.cilia.workbench.designer.view.chainview.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.element.Adapter;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.TextListDialog;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.common.XMLChain;
import fr.liglab.adele.cilia.workbench.designer.service.element.jarreposervice.CiliaJarRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class NewAdapterDialog extends TextListDialog {

	private final XMLChain chain;

	public NewAdapterDialog(Shell parentShell, XMLChain chain) {
		super(parentShell, "New Adapter", "id", "type", getListValues());
		this.chain = chain;
	}

	private static Map<String, Object> getListValues() {
		Map<String, Object> retval = new HashMap<String, Object>();

		List<Adapter> list = new ArrayList<Adapter>();
		list.addAll(CiliaJarRepoService.getInstance().getAdapters());
		// list.addAll(SpecRepoService.getInstance().getAdapterSpecs());

		for (Adapter a : list) {
			NameNamespaceID id = (NameNamespaceID) a.getId();
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
		Adapter a = (Adapter) object;
		return chain.isNewComponentAllowed(id, (NameNamespaceID) a.getId());
	}
}
