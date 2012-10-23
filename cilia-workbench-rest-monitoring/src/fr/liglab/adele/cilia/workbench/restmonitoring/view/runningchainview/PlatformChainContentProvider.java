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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChainContentProvider implements IGraphEntityContentProvider {

	private final PlatformChain model;

	public PlatformChainContentProvider(PlatformChain model) {
		this.model = model;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO lien avec le modèle
		if (inputElement == null)
			return new Object[0];
		else
			return new Object[0];
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		// TODO lien avec le modèle
		if (entity == null)
			return new Object[0];
		else
			return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
