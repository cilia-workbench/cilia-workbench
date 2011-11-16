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
package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.Iterator;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

public class PullElementUtil {
	
	public static RepoElement pullRepoElement(List<RepoElement> newInstance, String id) {
		for (Iterator<RepoElement> itr = newInstance.iterator(); itr.hasNext();) {
			RepoElement element = itr.next();
			if (element.getFilePath().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
	
	public static Binding pullBinding(Chain newInstance, String from, String to) {
		for (Iterator<Binding> itr = newInstance.getBindings().iterator(); itr.hasNext();) {
			Binding element = itr.next();
			if (element.getSourceId().equals(from) && element.getDestinationId().equals(to)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static MediatorInstance pullMediatorInstance(Chain newInstance, String id) {
		for (Iterator<MediatorInstance> itr = newInstance.getMediators().iterator(); itr.hasNext();) {
			MediatorInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static AdapterInstance pullAdapterInstance(Chain newInstance, String id) {
		for (Iterator<AdapterInstance> itr = newInstance.getAdapters().iterator(); itr.hasNext();) {
			AdapterInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
	
	public static Chain pullChain(Dscilia newInstance, String id) {
		for (Iterator<Chain> itr = newInstance.getChains().iterator(); itr.hasNext();) {
			Chain element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
