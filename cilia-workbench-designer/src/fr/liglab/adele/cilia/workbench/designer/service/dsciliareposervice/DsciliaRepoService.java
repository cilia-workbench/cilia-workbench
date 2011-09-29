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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Dscilia;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * A central place for managing the DScilia repository. The repository can be
 * asked to refresh the model. The repository can be asked to send model update
 * notifications.
 */
public class DsciliaRepoService {

	/** The repository */
	private List<RepoElement> repo = new ArrayList<RepoElement>();

	/** Singleton instance */
	private static DsciliaRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH;

	/** Listeners. */
	private List<IDSciliaRepositoryListener> listeners = new ArrayList<IDSciliaRepositoryListener>();

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the instance.
	 */
	public static DsciliaRepoService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DsciliaRepoService();
		return INSTANCE;
	}

	/**
	 * Constructor. Registers for repository path update and constructs the
	 * model.
	 */
	private DsciliaRepoService() {
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

	public List<RepoElement> getModel() {
		return repo;
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
	 * Updates the model and sends notifications.
	 */
	public void updateModel() {

		// Gets dscilia file list in the physical repository
		File dir = new File(getRepositoryPath());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".dscilia");
			}
		});

		// Creates a new model from the repository
		List<RepoElement> elements = new ArrayList<RepoElement>();
		if (list != null) {
			for (File jar : list) {
				String path = jar.getPath();
				try {
					Dscilia dscilia = new Dscilia(path);
					elements.add(new RepoElement(path, dscilia));
				} catch (Exception e) {
					e.printStackTrace();
					elements.add(new RepoElement(path, null));
				}
			}
		}

		// Updates existing model with computed model
		Changeset[] changes = merge(elements);

		// Sends notifications
		notifyListeners(changes);
	}
	
	/**
	 * Notifies listeners with given change set table.
	 * 
	 * @param changes
	 *            the change set table.
	 */
	private void notifyListeners(Changeset[] changes) {
		for (IDSciliaRepositoryListener listener : listeners) {
			listener.repositoryChange(changes);
		}
	}

	/**
	 * Register listener.
	 *
	 * @param listener the listener
	 */
	public void registerListener(IDSciliaRepositoryListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}
	
	
	/**
	 * Unregister listener.
	 *
	 * @param listener the listener
	 * @return true, if successful
	 */
	public boolean unregisterListener(IDSciliaRepositoryListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}
	
	
	public Changeset[] merge(List<RepoElement> repoElements) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<RepoElement> itr = repo.iterator(); itr.hasNext();) {
			RepoElement old = itr.next();
			String id = old.getFilePath();
			RepoElement updated = pullRepoElement(repoElements, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (RepoElement r : repoElements) {
			repo.add(r);
			retval.add(new Changeset(Operation.ADD, r));
		}

		return retval.toArray(new Changeset[0]);
	}

	private RepoElement pullRepoElement(List<RepoElement> newInstance, String id) {
		for (Iterator<RepoElement> itr = newInstance.iterator(); itr.hasNext();) {
			RepoElement element = itr.next();
			if (element.getFilePath().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
