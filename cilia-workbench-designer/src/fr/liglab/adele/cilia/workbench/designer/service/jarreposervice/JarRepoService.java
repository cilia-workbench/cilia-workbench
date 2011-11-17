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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;

/**
 * A central place for managing the Jar repository. The repository can be asked
 * to refresh the model. The repository can be asked to send model update
 * notifications.
 * 
 * @author Etienne Gandrille
 */
public class JarRepoService {

	/** Singleton instance. */
	private static JarRepoService INSTANCE;

	/** The repository content. */
	private Bundle[] repo;

	/** Listeners. */
	private List<IJarRepositoryListener> listeners = new ArrayList<IJarRepositoryListener>();

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the instance.
	 */
	public static JarRepoService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JarRepoService();
		return INSTANCE;
	}

	/**
	 * Instantiates a new jar repo service.
	 */
	private JarRepoService() {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH)) {
					updateModel();
				}
			}
		});
		updateModel();
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public Bundle[] getModel() {
		return repo;
	}

	/**
	 * Gets the repository path on the file system.
	 * 
	 * @return the repository path
	 */
	public String getRepositoryPath() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getString(CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH);
	}

	/**
	 * Updates the model and sends notifications.
	 */
	public void updateModel() {
		File dir = new File(getRepositoryPath());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		List<Bundle> bundles = new ArrayList<Bundle>();
		if (list != null) {
			for (File jar : list) {
				try {
					String path = jar.getPath();
					bundles.add(new Bundle(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Updates model with computed one
		repo = bundles.toArray(new Bundle[0]);

		// Sends notifications
		notifyListeners();
	}

	/**
	 * Notifies listeners the model have been updated.
	 * 
	 */
	private void notifyListeners() {
		for (IJarRepositoryListener listener : listeners) {
			listener.jarRepositoryContentUpdated();
		}
	}

	/**
	 * Register listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(IJarRepositoryListener listener) {
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
	public boolean unregisterListener(IJarRepositoryListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}

	/**
	 * Gets the mediators id.
	 * 
	 * @return the mediators id
	 */
	public String[] getMediatorsId() {
		List<String> retval = new ArrayList<String>();
		for (Bundle bundle : repo)
			for (MediatorComponent mc : bundle.getMetadata().getMediatorComponents())
				retval.add(mc.getName());

		return retval.toArray(new String[0]);
	}

	/**
	 * Gets the adapters id.
	 * 
	 * @return the adapters id
	 */
	public String[] getAdaptersId() {
		List<String> retval = new ArrayList<String>();
		for (Bundle bundle : repo)
			for (Adapter a : bundle.getMetadata().getAdapters())
				retval.add(a.getName());

		return retval.toArray(new String[0]);
	}

	/**
	 * Gets an adapter, using its name.
	 * 
	 * @param name
	 *            the name
	 * @return the adapter
	 */
	public Adapter getAdapter(String name) {
		for (Bundle bundle : repo)
			for (Adapter a : bundle.getMetadata().getAdapters())
				if (a.getName().equalsIgnoreCase(name))
					return a;
		return null;
	}

	/**
	 * Gets a mediator, using its name.
	 * 
	 * @param name
	 *            the name
	 * @return the mediator
	 */
	public MediatorComponent getMediator(String name) {
		for (Bundle bundle : repo)
			for (MediatorComponent m : bundle.getMetadata().getMediatorComponents())
				if (m.getName().equalsIgnoreCase(name))
					return m;
		return null;
	}
}
