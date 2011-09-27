/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.BindingReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview.NodeElement;

/**
 * The Class GraphContentProvider.
 */
public class GraphContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.viewers.IGraphEntityContentProvider#getConnectedTo(java.lang.Object)
	 */
	@Override
	public Object[] getConnectedTo(Object entity) {

		if (entity instanceof NodeElement) {
			BindingReadOnly bindings[];
			MediatorReadOnly element = ((NodeElement) entity).getElement();
			bindings = element.getOutBindings();
			
			List<Object> retval = new ArrayList<Object>();
			if (bindings != null) {
				for (BindingReadOnly binding : bindings) {
					retval.add(new NodeElement(binding.getTargetMediator()));
				}
			}
			return retval.toArray();
		}

		throw new RuntimeException("Type not supported");
	}
}
