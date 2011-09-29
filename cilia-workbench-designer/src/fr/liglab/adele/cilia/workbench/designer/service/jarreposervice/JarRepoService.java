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
package fr.liglab.adele.cilia.workbench.designer.service.jarreposervice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Bundle;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;

/**
 * JarRepoService.
 */
public class JarRepoService {

	/** Singleton instance */
	private static JarRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;

	/** The service model */
	private Bundle[] model;
	
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
				if (event.getProperty().equals(PREFERENCE_PATH_KEY)) {
					updateModel();
				}
			}
		});
		updateModel();
	}
	
	public Bundle[] getModel() {
		return model;
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
		model = bundles.toArray(new Bundle[0]);
		
		// Sends notifications
		notifyListeners();
	}
	
	/**
	 * Notifies listeners with given change set table.
	 * 
	 * @param changes
	 *            the change set table.
	 */
	private void notifyListeners() {
		for (IJarRepositoryListener listener : listeners) {
			listener.repositoryContentUpdated();
		}
	}

	/**
	 * Register listener.
	 *
	 * @param listener the listener
	 */
	public void registerListener(IJarRepositoryListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}
	
	
	/**
	 * Unregister listener.
	 *
	 * @param listener the listener
	 * @return true, if successful
	 */
	public boolean unregisterListener(IJarRepositoryListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}
}
