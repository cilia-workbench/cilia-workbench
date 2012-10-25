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
package fr.liglab.adele.cilia.workbench.common.selectionservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;

/**
 * IMPORTANT! This class have been implemented because the selection service in
 * Eclipse Juno doesn't work well. For evolutions, it's important to follow
 * selection service API, to make refactoring easier...
 * 
 * @author Etienne Gandrille
 */
public class SelectionService {

	private Map<String, PartManager> managers = new HashMap<String, PartManager>();

	private static SelectionService INSTANCE = new SelectionService();

	private SelectionService() {
	}

	public static SelectionService getInstance() {
		return INSTANCE;
	}

	public void addSelectionListener(String partId, SelectionListener listener) {
		PartManager manager = managers.get(partId);
		if (manager == null) {
			manager = new PartManager(partId);
			managers.put(partId, manager);
		}

		manager.addSelectionListener(listener);
	}

	public void removeSelectionListener(String partId, SelectionListener listener) {
		PartManager manager = managers.get(partId);
		if (manager != null) {
			manager.removeListener(listener);
		}
	}

	public void addSelectionProvider(String partId, Viewer viewer) {
		PartManager manager = managers.get(partId);
		if (manager == null) {
			manager = new PartManager(partId);
			managers.put(partId, manager);
		}

		manager.setViewer(viewer);
	}

	public void removeSelectionProvider(String partId) {
		PartManager manager = managers.get(partId);
		if (manager != null) {
			manager.disposeViewer();
		}
	}

	private class PartManager implements ISelectionChangedListener {

		private final String partId;
		private Viewer viewer = null;

		private List<SelectionListener> listeners = new ArrayList<SelectionListener>();

		public PartManager(String partId) {
			this.partId = partId;
		}

		public void disposeViewer() {
			viewer.removeSelectionChangedListener(this);
			viewer = null;
		}

		public void removeListener(SelectionListener listener) {
			listeners.remove(listener);
		}

		public void addSelectionListener(SelectionListener listener) {
			if (!listeners.contains(listener))
				listeners.add(listener);
		}

		public void setViewer(Viewer viewer) {
			if (this.viewer == null) {
				this.viewer = viewer;
				viewer.addSelectionChangedListener(this);
			} else
				throw new RuntimeException("a single viewer can be registered by view ID in this implementation...");
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (event != null && event.getSelection() != null) {
				for (SelectionListener listener : listeners)
					listener.selectionChanged(partId, event.getSelection());
			}
		}
	}
}
