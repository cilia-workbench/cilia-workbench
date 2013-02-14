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
package fr.liglab.adele.cilia.workbench.common.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.files.FileUtil;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.ciliaerrorview.CiliaMarkerUtil;

/**
 * Represents a repository, from a model point of view.
 * 
 * 
 * @param <FileType>
 *            the elements hosted by the repository.
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractRepoService<FileType extends AbstractFile<ModelType>, ModelType> {

	/** The repository content. */
	protected List<FileType> repoContent;

	/** Listeners used to notify repository updates. */
	private List<IRepoServiceListener> listeners;

	/** Repository name */
	private final String name;

	/**
	 * A content provider. Usualy, it's useful for displaying the repository in
	 * a view. Here, it's used for navigating the repository.
	 */
	protected GenericContentProvider contentProvider;

	/**
	 * The preference key, used to find the physical repository on the hard
	 * disk.
	 */
	private final String PREFERENCE_PATH_KEY;

	/** Extension used by files hosted in the repository. */
	public final String ext;

	protected AbstractRepoService(String preferenceKey, String ext, String repoName) {
		PREFERENCE_PATH_KEY = preferenceKey;
		this.ext = ext;
		this.name = repoName;

		Activator.getInstance().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PREFERENCE_PATH_KEY)) {
					updateModel();
				}
			}
		});

		repoContent = new ArrayList<FileType>();
		listeners = new ArrayList<IRepoServiceListener>();
	}

	public FileType getFileFromName(String fileName) {
		for (FileType element : repoContent)
			if (element.getFilename().equals(fileName))
				return element;
		return null;
	}

	/**
	 * Callback method used as soon as the model should be updated.
	 */
	public abstract void updateModel();

	/**
	 * Updates markers for this repository.
	 * 
	 * IMPORTANT : Only the elements managed by the content provider are tested
	 * to see if it implements {@link ErrorsAndWarningsFinder} interface.
	 */
	protected void updateMarkers() {

		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		// removes markers
		try {
			CiliaMarkerUtil.deleteMarkers(this);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		// finds errors and warnings
		if (this instanceof ErrorsAndWarningsFinder)
			for (CiliaFlag flag : ((ErrorsAndWarningsFinder) this).getErrorsAndWarnings())
				list.add(flag);

		Set<Object> elements = getContentProvider().getAllElements();
		for (Object element : elements)
			if (element instanceof ErrorsAndWarningsFinder)
				for (CiliaFlag flag : ((ErrorsAndWarningsFinder) element).getErrorsAndWarnings())
					list.add(flag);

		// creates markers
		for (CiliaFlag flag : list)
			CiliaMarkerUtil.createMarker(flag.getSeverity(), flag.getMessage(), this, flag.getSourceProvider());
	}

	/**
	 * Gets the repository path on the file system.
	 * 
	 * @return the repository path
	 */
	public File getRepositoryLocation() {
		IPreferenceStore store = Activator.getInstance().getPreferenceStore();
		File dir = new File(store.getString(PREFERENCE_PATH_KEY));

		if (!dir.isDirectory())
			return null;

		return dir;
	}

	/**
	 * Gets files list of the physical repository.
	 * 
	 * @return the files
	 */
	protected File[] getFiles() {
		File dir = getRepositoryLocation();
		return FileUtil.getFiles(dir, ext);
	}

	public List<FileType> getRepoContent() {
		return repoContent;
	}

	public GenericContentProvider getContentProvider() {
		return contentProvider;
	}

	public void registerListener(IRepoServiceListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}

	public boolean unregisterListener(IRepoServiceListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}

	protected void notifyListeners(List<Changeset> changes) {
		for (IRepoServiceListener listener : listeners)
			listener.repositoryContentUpdated(this, changes);
	}

	/**
	 * Tests if a name uses valid characters. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param name
	 *            file name to be tested
	 * @return null if the name is valid, an error message (including "")
	 *         otherwise.
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
	 * Tests if a file can be created with the given fileName. This method
	 * follows {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param fileName
	 *            file name to be tested
	 * @return null if the name is valid, an error message (including "")
	 *         otherwise.
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
		File dir = getRepositoryLocation();
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
	 * Returns a string, which contains the text automatically added in a new
	 * file of this repository.
	 * 
	 * @return the content for new file.
	 */
	protected abstract String getContentForNewFile(String... parameters);

	/**
	 * Creates a new file in the repository. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param fileName
	 *            the file name
	 * @param content
	 *            the file content
	 * @return null if success, an error message otherwise.
	 */
	public String createFile(String fileName, String... parameters) {
		File destination = new File(getRepositoryLocation(), fileName);

		if (isNewFileNameAllowed(fileName) != null)
			return "file name is not allowed : " + isNewFileNameAllowed(fileName);

		// write
		boolean hasError = false;
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(destination));
			out.write(getContentForNewFile(parameters));
		} catch (IOException e) {
			e.printStackTrace();
			hasError = true;
		}

		// close writer
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				hasError = true;
				e.printStackTrace();
			}
		}

		updateModel();

		// return
		if (hasError)
			return "i/o error while writing file";
		else
			return null;
	}

	public boolean deleteRepoElement(AbstractFile<?> element) {
		boolean retval = element.getResource().delete();
		updateModel();
		return retval;
	}

	/**
	 * Returns an array with all non null abstract elements. Remember this
	 * repository holds concrete elements. Each concrete elements points to an
	 * abstract element, which can be null if the concrete element is in a non
	 * valid state.
	 * 
	 * @return
	 */
	public List<ModelType> findAbstractElements() {

		List<ModelType> retval = new ArrayList<ModelType>();

		for (FileType element : repoContent) {
			ModelType abstractModel = element.getModel();
			if (abstractModel != null)
				retval.add(abstractModel);
		}

		return retval;
	}

	@Override
	public String toString() {
		return Strings.nullToEmpty(name);
	}
}
