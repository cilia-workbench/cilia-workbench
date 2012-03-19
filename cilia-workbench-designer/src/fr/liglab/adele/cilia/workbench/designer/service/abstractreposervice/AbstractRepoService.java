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

/**
 * Represents a repository, from a model point of view.
 * 
 * 
 * @param <ModelType>
 *            the elements hosted by the repository.
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractRepoService<ModelType> {

	/** The repository content. */
	protected List<ModelType> model;

	/** Listeners used to notify repository updates. */
	private List<IRepoServiceListener> listeners;

	/**
	 * A content provider. Usualy, it's useful for displaying the repository in a view. Here, it's used for navigating
	 * the repository.
	 */
	protected GenericContentProvider contentProvider;

	/**
	 * The preference key, used to find the physical repository on the hard disk.
	 */
	private final String PREFERENCE_PATH_KEY;

	/** Extension used by files hosted in the repository. */
	private final String ext;

	/**
	 * Instantiates a new repository.
	 * 
	 * @param preferenceKey
	 *            the preference key
	 * @param ext
	 *            the extension
	 */
	protected AbstractRepoService(String preferenceKey, String ext) {
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
		listeners = new ArrayList<IRepoServiceListener>();

		updateModel();
	}

	/**
	 * Callback method used as soon as the model should be updated.
	 */
	public abstract void updateModel();

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
	 * Gets files list in the physical repository.
	 * 
	 * @return the files
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

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public List<ModelType> getModel() {
		return model;
	}

	/**
	 * Gets the content provider.
	 * 
	 * @return the content provider
	 */
	public GenericContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * Register a new listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(IRepoServiceListener listener) {
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
	public boolean unregisterListener(IRepoServiceListener listener) {
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
		for (IRepoServiceListener listener : listeners) {
			listener.repositoryContentUpdated(changes);
		}
	}
}
