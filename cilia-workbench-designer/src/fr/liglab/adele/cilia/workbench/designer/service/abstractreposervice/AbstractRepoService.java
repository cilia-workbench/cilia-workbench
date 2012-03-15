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
package fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.GenericContentProvider;


public abstract class AbstractRepoService<ModelType> {

	private final String PREFERENCE_PATH_KEY;

	protected List<ModelType> model;
	
	protected List<IAbstractRepoServiceListener> listeners;
	
	/** Content provider, for computing parents */
	protected GenericContentProvider contentProvider;
	
	/** Files extension. */
	private final String ext;

	public AbstractRepoService(String preferenceKey, String ext) {
		PREFERENCE_PATH_KEY = preferenceKey;
		this.ext = ext;

		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PREFERENCE_PATH_KEY)) {
					updateModel();
				}
			}
		});
		
		model = new ArrayList<ModelType>();
		listeners = new ArrayList<IAbstractRepoServiceListener>();

		updateModel();
	}

	/**
	 * Gets the repository path on the file system.
	 * 
	 * @return the repository path
	 */
	public String getRepositoryPath() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getString(PREFERENCE_PATH_KEY);
	}

	/**
	 * Gets files list in the physical repository
	 * @return
	 */
	protected File[] getFiles() {
		File dir = new File(getRepositoryPath());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(ext);
			}
		});
		
		if (list == null)
			return new File[0];
		else
			return list;
	}

	public abstract void updateModel();
	
	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public List<ModelType> getModel() {
		return model;
	}
	
	public GenericContentProvider getContentProvider() {
		return contentProvider;
	}
	
	/**
	 * Register listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(IAbstractRepoServiceListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Unregister listener.
	 * 
	 * @param listener
	 *            the listener
	 * @return true, if successful
	 */
	public boolean unregisterListener(IAbstractRepoServiceListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}
	
	/**
	 * Notifies listeners with given change set table.
	 * 
	 * @param changes
	 *            the change set table.
	 */
	protected void notifyListeners(Changeset[] changes) {
		for (IAbstractRepoServiceListener listener : listeners) {
			listener.repositoryContentUpdated(changes);
		}
	}
}
