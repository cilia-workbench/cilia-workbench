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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;

/**
 * 
 * @author Etienne Gandrille
 */
public class ChainGraphContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	private Chain model = null;

	public ChainGraphContentProvider() {
	}

	@Override
	public Object[] getConnectedTo(Object entity) {

		if (model == null)
			return new Object[0];

		if (entity instanceof AdapterInstance) {
			AdapterInstance ai = (AdapterInstance) entity;
			return model.getDestinations(ai);

		} else if (entity instanceof MediatorInstance) {
			MediatorInstance mi = (MediatorInstance) entity;
			return model.getDestinations(mi);
		}

		return new Object[0];
	}

	public void setModel(Chain model) {
		this.model = model;
	}
}
