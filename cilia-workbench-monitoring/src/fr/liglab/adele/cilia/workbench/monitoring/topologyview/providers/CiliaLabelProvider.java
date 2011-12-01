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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;
import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.providers.GenericLabelProvider;

/**
 * Label provider for the tree viewer.
 */
public class CiliaLabelProvider extends GenericLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object obj) {
		if (obj instanceof MonitoredApplication)
			return null;
		if (obj instanceof CiliaContextReadOnly)
			return "Platform";
		if (obj instanceof ChainReadOnly)
			return ((ChainReadOnly) obj).getId();
		if (obj instanceof AdapterReadOnly)
			return ((AdapterReadOnly) obj).getId();
		if (obj instanceof MediatorReadOnly)
			return ((MediatorReadOnly) obj).getId();

		if (obj instanceof EntityConnectionData)
			return "";

		throw new RuntimeException("Unsupported type: " + obj.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public String getImageKey(Object obj) {
		if (obj instanceof CiliaContextReadOnly)
			return Activator.CHAIN;
		else if (obj instanceof ChainReadOnly)
			return Activator.CHAIN;
		else if (obj instanceof AdapterReadOnly) {
			AdapterReadOnly adapter = (AdapterReadOnly) obj;

			// adapter.getPattern().equal(PatternType.IN_ONLY)
			// adapter.getPattern().equal(PatternType.OUT_ONLY)

			int in = adapter.getInBindings() == null ? 0 : adapter.getInBindings().length;
			int out = adapter.getOutBindings() == null ? 0 : adapter.getOutBindings().length;

			if (in == 0 && out != 0)
				return Activator.IN_ADAPTER;
			else if (in != 0 && out == 0)
				return Activator.OUT_ADAPTER;
			else
				return Activator.MEDIATOR;
		} else if (obj instanceof MediatorReadOnly)
			return Activator.MEDIATOR;
		else if (obj instanceof EntityConnectionData) {
			return null;
		} else
			throw new RuntimeException("Unsupported type: " + obj.getClass());
	}
}
