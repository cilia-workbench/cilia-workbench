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

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.TopologyView;

/**
 * Content provider for the tree viewer.
 * @author Etienne Gandrille
 */
public class TreeContentProvider implements ITreeContentProvider {

	/** The view, used to find the model. */
	private TopologyView view;

	/**
	 * Constructor.
	 *
	 * @param view the view, used to find the model.
	 */
	public TreeContentProvider(TopologyView view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object child) {
		if (child instanceof MonitoredApplication)
			return null;
		if (child instanceof CiliaContextReadOnly)
			return view.getModel();
		if (child instanceof ChainReadOnly)
			return view.getModel().getCiliaContextRO();
		if (child instanceof AdapterReadOnly)
			return CiliaUtil.getAdapterParent(view.getModel().getCiliaContextRO(), (AdapterReadOnly) child);
		if (child instanceof MediatorReadOnly)
			return CiliaUtil.getMediatorParent(view.getModel().getCiliaContextRO(), (MediatorReadOnly) child);

		throw new RuntimeException("Unsupported type: " + child.getClass());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getChildren(Object parent) {
		if (parent instanceof MonitoredApplication) {
			CiliaContextReadOnly ccro = ((MonitoredApplication) parent).getCiliaContextRO();
			return new Object[] { ccro };
		}
		if (parent instanceof CiliaContextReadOnly)
			return ((CiliaContextReadOnly) parent).getAllChains().toArray();
		if (parent instanceof ChainReadOnly) {
			ArrayList<Object> retval = new ArrayList<Object>();
			ChainReadOnly chain = (ChainReadOnly) parent;
			retval.addAll(chain.getAdapters());
			retval.addAll(chain.getMediators());
			return retval.toArray();
		}
		if (parent instanceof AdapterReadOnly)
			return new Object[0];
		if (parent instanceof MediatorReadOnly)
			return new Object[0];

		throw new RuntimeException("Unsupported type: " + parent.getClass());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object parent) {
		// Immediate answer
		if (parent instanceof MonitoredApplication)
			return true;
		if (parent instanceof AdapterReadOnly)
			return false;
		if (parent instanceof MediatorReadOnly)
			return false;

		// Computation needed
		return getChildren(parent).length != 0;
	}
}
