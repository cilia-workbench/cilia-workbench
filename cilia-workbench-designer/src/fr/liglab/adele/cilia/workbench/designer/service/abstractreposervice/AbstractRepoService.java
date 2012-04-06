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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.parser.common.AbstractFile;

/**
 * Represents a repository, from a model point of view.
 * 
 * 
 * @param <ModelType>
 *            the elements hosted by the repository.
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractRepoService<ModelType extends AbstractFile<AbstractType>, AbstractType> {

	/** The repository content. */
	protected List<ModelType> model;

	/** Listeners used to notify repository updates. */
	private List<IRepoServiceListener> listeners;

	/** Repository name */
	private final String name;

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
	public final String ext;

	/**
	 * Instantiates a new repository.
	 * 
	 * @param preferenceKey
	 *            the preference key
	 * @param ext
	 *            the extension
	 */
	protected AbstractRepoService(String preferenceKey, String ext, String repoName) {
		PREFERENCE_PATH_KEY = preferenceKey;
		this.ext = ext;
		this.name = repoName;

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

	/**
	 * Tests if a name uses valid characters. This method follows {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param name
	 *            file name to be tested
	 * @return null if the name is valid, an error message (including "") otherwise.
	 */
	public String isNameUsesAllowedChar(final String name) {

		/* Null value */
		if (name == null)
			return "name can't be null";

		/* Individual char checking */
		char[] chars = new char[name.length()];
		name.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			boolean nb = (chars[i] >= '0' && chars[i] <= '9');
			boolean min = (chars[i] >= 'a' && chars[i] <= 'z');
			boolean maj = (chars[i] >= 'A' && chars[i] <= 'Z');
			boolean spec = (chars[i] == '-' || chars[i] == '_' || chars[i] == '.');

			boolean valid = (nb || min || maj || spec);

			if (!valid) {
				return "character '" + chars[i] + "' is not allowed";
			}
		}

		return null;
	}

	/**
	 * Tests if a file can be created with the given fileName. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param fileName
	 *            file name to be tested
	 * @return null if the name is valid, an error message (including "") otherwise.
	 */
	public String isNewFileNameAllowed(final String fileName) {

		/* Char checking */
		if (isNameUsesAllowedChar(fileName) != null)
			return isNameUsesAllowedChar(fileName);

		/* Extension checking */
		if (!fileName.toLowerCase().endsWith(ext))
			return "file name must end with '" + ext + "'";

		/* Length checking */
		if (fileName.length() == ext.length())
			return "file name can't be empty";

		/* Already exists checking */
		File dir = new File(getRepositoryPath());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase(fileName);
			}
		});
		if (list.length != 0)
			return "File already exists in repository";

		return null;
	}

	/**
	 * Returns a string, which contains the text automatically added in a new file of this repository.
	 * 
	 * @return the content for new file.
	 */
	protected abstract String getContentForNewFile();

	/**
	 * Creates a new file in the repository. This method follows {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param fileName
	 *            the file name
	 * @param content
	 *            the file content
	 * @return null if success, an error message otherwise.
	 */
	public String createFile(String fileName) {

		String content = getContentForNewFile();

		if (isNewFileNameAllowed(fileName) != null)
			return "file name is not allowed : " + isNewFileNameAllowed(fileName);

		String repoPath = getRepositoryPath();
		String path;
		if (repoPath.endsWith(File.separator))
			path = repoPath + fileName;
		else
			path = repoPath + File.separator + fileName;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "i/o error while writing file";
		}

		updateModel();
		return null;
	}

	/**
	 * Delete an element in the file system repository.
	 * 
	 * @param element
	 */
	public boolean deleteRepoElement(AbstractFile<?> element) {
		File file = new File(element.getFilePath());
		boolean retval = file.delete();
		updateModel();
		return retval;
	}

	/**
	 * Returns an array with all non null abstract elements. Remember this repository holds concrete elements. Each
	 * concrete elements points to an abstract element, which can be null if the concrete element is in a non valid
	 * state.
	 * 
	 * @return
	 */
	public List<AbstractType> findAbstractElements() {

		List<AbstractType> retval = new ArrayList<AbstractType>();

		for (ModelType element : model) {
			AbstractType abstractModel = element.getModel();
			if (abstractModel != null)
				retval.add(abstractModel);
		}

		return retval;
	}

	@Override
	public String toString() {
		return name;
	}
}
