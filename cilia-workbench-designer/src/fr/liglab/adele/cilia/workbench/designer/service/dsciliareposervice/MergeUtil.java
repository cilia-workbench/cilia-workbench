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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.util.Iterator;
import java.util.List;

/**
 * A few helper methods, for the merge feature.
 * 
 * @author Etienne Gandrille
 */
public class MergeUtil {

	/**
	 * Remove an element from {@link RepoElement}, and returns it.
	 * 
	 * @param repoElements
	 *            a list of {@link RepoElement}
	 * @param path
	 *            the path, to match a specific {@link RepoElement}
	 * @return the {@link RepoElement} found and removed from the repoElements,
	 *         or null if not found.
	 */
	public static RepoElement pullRepoElement(List<RepoElement> repoElements, String path) {
		for (Iterator<RepoElement> itr = repoElements.iterator(); itr.hasNext();) {
			RepoElement element = itr.next();
			if (element.getFilePath().equals(path)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	/**
	 * Remove a binding from a chain, and returns it.
	 * 
	 * @param chain
	 *            the chain
	 * @param from
	 *            the binding source
	 * @param to
	 *            the binding destination
	 * @return the {@link Binding} found and removed from the chain, or null if
	 *         not found.
	 */
	public static Binding pullBinding(Chain chain, String from, String to) {
		for (Iterator<Binding> itr = chain.getBindings().iterator(); itr.hasNext();) {
			Binding element = itr.next();
			if (element.getSourceId().equals(from) && element.getDestinationId().equals(to)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	/**
	 * Remove a {@link MediatorInstance} from a chain, and returns it.
	 * 
	 * @param chain
	 *            the chain
	 * @param mediatorId
	 *            the id of the mediator to be found.
	 * @return the {@link MediatorInstance} found and removed from the chain, or
	 *         null if not found.
	 */
	public static MediatorInstance pullMediatorInstance(Chain chain, String mediatorId) {
		for (Iterator<MediatorInstance> itr = chain.getMediators().iterator(); itr.hasNext();) {
			MediatorInstance element = itr.next();
			if (element.getId().equals(mediatorId)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	/**
	 * Remove a {@link AdapterInstance} from a chain, and returns it.
	 * 
	 * @param chain
	 *            the chain
	 * @param adapterId
	 *            the adapter id
	 * @return the {@link AdapterInstance} found and removed from the chain, or
	 *         null if not found.
	 */
	public static AdapterInstance pullAdapterInstance(Chain chain, String adapterId) {
		for (Iterator<AdapterInstance> itr = chain.getAdapters().iterator(); itr.hasNext();) {
			AdapterInstance element = itr.next();
			if (element.getId().equals(adapterId)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	/**
	 * Remove a {@link Chain} from a {@link Dscilia}, and returns it.
	 * 
	 * @param dscilia
	 *            the dscilia
	 * @param chainId
	 *            the id of the chain to be searched.
	 * @return the {@link Chain} found and removed from the {@link Dscilia}, or
	 *         null if not found.
	 */
	public static Chain pullChain(Dscilia dscilia, String chainId) {
		for (Iterator<Chain> itr = dscilia.getChains().iterator(); itr.hasNext();) {
			Chain element = itr.next();
			if (element.getId().equals(chainId)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
