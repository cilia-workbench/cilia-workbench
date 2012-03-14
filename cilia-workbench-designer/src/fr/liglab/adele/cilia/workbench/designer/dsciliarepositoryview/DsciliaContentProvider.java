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
package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.GenericContentProvider;

public class DsciliaContentProvider extends GenericContentProvider {

	public DsciliaContentProvider(List<DsciliaFile> repo) {
		parent.put(repo, new Object[0]);
		children.put(repo, new ArrayList<Object>());

		for (DsciliaFile re : repo) {
			parent.put(re, repo);
			children.get(repo).add(re);
			children.put(re, new ArrayList<Object>());

			if (re.getModel() != null) {
				for (Chain c : re.getModel().getChains()) {
					parent.put(c, re);
					children.get(re).add(c);
					children.put(c, new ArrayList<Object>());
				}
			}
		}
	}
}
