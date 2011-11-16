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
package fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.common.base.Preconditions;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview.DsciliaContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset.Operation;

/**
 * A central place for managing the DScilia repository. The repository can be
 * asked to refresh the model. The repository can be asked to send model update
 * notifications.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaRepoService {

	/** The repository */
	private List<RepoElement> repo = new ArrayList<RepoElement>();

	/** Singleton instance */
	private static DsciliaRepoService INSTANCE;

	/** Listeners. */
	private List<IDsciliaRepositoryListener> listeners = new ArrayList<IDsciliaRepositoryListener>();

	/** DScilia files extension. */
	private final String ext = ".dscilia";

	/** Content provider, for computing parents */
	private DsciliaContentProvider contentProvider;

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
				if (event.getProperty().equals(CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH)) {
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
		return store.getString(CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH);
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
				return name.toLowerCase().endsWith(ext);
			}
		});

		// Creates a new model from the repository
		List<RepoElement> elements = new ArrayList<RepoElement>();
		if (list != null) {
			for (File jar : list) {
				String path = jar.getPath();
				try {
					elements.add(new RepoElement(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Updates existing model with computed model
		Changeset[] changes = merge(elements);

		// update content provider
		contentProvider = new DsciliaContentProvider(repo);

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
		for (IDsciliaRepositoryListener listener : listeners) {
			listener.repositoryChange(changes);
		}
	}

	/**
	 * Register listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(IDsciliaRepositoryListener listener) {
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
	public boolean unregisterListener(IDsciliaRepositoryListener listener) {
		if (listener != null)
			return listeners.remove(listener);
		else
			return false;
	}

	/**
	 * Merge a list of {@link RepoElement} into the current object. Differences
	 * between the argument and the current object are injected into the current
	 * object.
	 * 
	 * @param newInstance
	 *            an 'up-to-date' object
	 * @return a list of {@link Changeset}, which can be empty.
	 */
	protected Changeset[] merge(List<RepoElement> repoElements) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<RepoElement> itr = repo.iterator(); itr.hasNext();) {
			RepoElement old = itr.next();
			String id = old.getFilePath();
			RepoElement updated = MergeUtil.pullRepoElement(repoElements, id);
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

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}

	/**
	 * Tests if a dscilia can be created with the given fileName. This method
	 * follows {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param newText
	 *            file name to be tested
	 * @return null if the name is valid, an error message (including "")
	 *         otherwise.
	 */
	public String isNewFileNameAllowed(String newText) {
		final String baseName = canonizeFileName(newText);
		if (baseName.toLowerCase().endsWith(ext) && baseName.length() > ext.length()) {
			File dir = new File(getRepositoryPath());
			File[] list = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.equalsIgnoreCase(baseName);
				}
			});
			if (list.length != 0)
				return "File already exists";
			else
				return null;
		} else
			return "File name must end with .dscilia";
	}

	/**
	 * Creates a dscilia file in the repository with the given file name. Throws
	 * an exception if creation fails.
	 * 
	 * @param fileName
	 *            the file name
	 */
	public void createFile(String fileName) {
		String msg = isNewFileNameAllowed(fileName);
		if (msg != null)
			throw new RuntimeException("File with name " + fileName + " can't be created: " + msg);

		String repoPath = getRepositoryPath();
		String path;
		if (repoPath.endsWith(File.separator))
			path = repoPath + canonizeFileName(fileName);
		else
			path = repoPath + File.separator + canonizeFileName(fileName);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write("<cilia>\n");
			out.write("</cilia>");
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("I/O error while creating " + fileName, e);
		}

		updateModel();
		return;
	}

	/**
	 * Before file creation, a method for name canonization.
	 * 
	 * @param fileName
	 * @return the name canonized.
	 */
	private String canonizeFileName(String fileName) {
		return fileName.trim();
	}

	/**
	 * Delete an element in the file system repository. Throws an exception if
	 * deletion fails.
	 * 
	 * @param element
	 */
	public void deleteRepoElement(RepoElement element) {
		File file = new File(element.getFilePath());
		boolean success = file.delete();
		updateModel();
		if (!success)
			throw new RuntimeException("Can't delete repo element with path " + element.getFilePath());
	}

	/**
	 * Tests if a chain with a given name can be created. This method follows
	 * {@link IInputValidator#isValid(String)} API.
	 * 
	 * @param chainName
	 *            chain name to be tested
	 * @return null if the name is valid, an error message (including "")
	 *         otherwise.
	 */
	public String isNewChainNameAllowed(String chainName) {
		final String baseName = canonizeChainName(chainName);
		if (baseName.length() == 0) {
			return "Empty name is not allowed";
		}

		Chain chain = findChain(chainName);
		if (chain != null) {
			return "A chain with this name already exists in the repository.";
		}

		return null;
	}

	/**
	 * Finds a chain in the repository.
	 * 
	 * @param chainName
	 *            the chain name
	 * 
	 * @return the chain, or null, if not found.
	 */
	private Chain findChain(String chainName) {
		for (RepoElement re : repo) {
			if (re.getDscilia() != null) {
				for (Chain chain : re.getDscilia().getChains()) {
					if (chain.getId().equalsIgnoreCase(chainName))
						return chain;
				}
			}
		}
		return null;
	}

	/**
	 * Before chain creation, a method for name canonization.
	 * 
	 * @param chainName
	 * @return the name canonized.
	 */
	private String canonizeChainName(String chainName) {
		return chainName.trim();
	}

	/**
	 * Creates the chain in a repository element.
	 * 
	 * @param repo
	 *            the repo
	 * @param chainName
	 *            the chain name
	 */
	public void createChain(RepoElement repo, String chainName) {
		if (repo.getDscilia() == null)
			throw new RuntimeException("Can't create chain : DSCilia is null");
		String msg = isNewChainNameAllowed(chainName);
		if (msg != null)
			throw new RuntimeException("Chain name " + chainName + " is not allowed");

		try {
			repo.getDscilia().createChain(chainName);
		} catch (MetadataException e) {
			throw new RuntimeException("Error while creating chain", e);
		}
	}

	/**
	 * Delete a chain.
	 * 
	 * @param chain
	 *            the chain name
	 */
	public void deleteChain(Chain chain) {
		RepoElement repo = (RepoElement) contentProvider.getParent(chain);
		if (repo == null)
			throw new RuntimeException("Can't delete chain : can't find the underlying RepoElement");
		try {
			repo.getDscilia().deleteChain(chain.getId());
		} catch (MetadataException e) {
			throw new RuntimeException("Error while deleting chain", e);
		}
	}

	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * Gets the {@link RepoElement} of an Object. Returns null if not found.
	 * 
	 * @param object
	 *            the object
	 * @return the {@link RepoElement}
	 */
	public RepoElement getRepoElement(Object object) {
		Preconditions.checkNotNull(object);

		if (object instanceof RepoElement)
			return (RepoElement) object;
		Object parent = getContentProvider().getParent(object);
		if (parent != null)
			return getRepoElement(parent);
		else
			return null;
	}

	/**
	 * Creates a mediator instance in a DSCilia.
	 * 
	 * @param chain
	 *            the chain name
	 * @param id
	 *            the mediator id
	 * @param type
	 *            the mediator type
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public void createMediatorInstance(Chain chain, String id, String type) throws MetadataException {
		RepoElement repo = (RepoElement) contentProvider.getParent(chain);
		if (repo == null)
			throw new RuntimeException("Can't create mediator instance : can't find the underlying RepoElement");

		String msg = chain.isNewMediatorInstanceAllowed(id, type);
		if (msg == null)
			repo.getDscilia().createMediatorInstance(chain, id, type);
		else
			throw new RuntimeException("Can't create mediator instance: " + msg);
	}

	/**
	 * Creates an adapter instance.
	 * 
	 * @param chain
	 *            the chain name
	 * @param id
	 *            the adapter id
	 * @param type
	 *            the adapter type
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public void createAdapterInstance(Chain chain, String id, String type) throws MetadataException {
		RepoElement repo = (RepoElement) contentProvider.getParent(chain);
		if (repo == null)
			throw new RuntimeException("Can't create adapter instance : can't find the underlying RepoElement");

		String msg = chain.isNewAdapterInstanceAllowed(id, type);
		if (msg == null)
			repo.getDscilia().createAdapterInstance(chain, id, type);
		else
			throw new RuntimeException("Can't create adapter instance: " + msg);
	}

	/**
	 * Creates a binding.
	 * 
	 * @param chain
	 *            the chain
	 * @param srcElem
	 *            the src elem
	 * @param srcPort
	 *            the src port
	 * @param dstElem
	 *            the dst elem
	 * @param dstPort
	 *            the dst port
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public void createBinding(Chain chain, String srcElem, String srcPort, String dstElem, String dstPort)
			throws MetadataException {
		RepoElement repo = (RepoElement) contentProvider.getParent(chain);
		if (repo == null)
			throw new RuntimeException("Can't create binding : can't find the underlying RepoElement");

		String msg = chain.isNewBindingAllowed(srcElem, srcPort, dstElem, dstPort);
		if (msg == null)
			repo.getDscilia().createBinding(chain, srcElem, srcPort, dstElem, dstPort);
		else
			throw new RuntimeException("Can't create binding: " + msg);
	}

	/**
	 * Deletes a binding.
	 * 
	 * @param chain
	 *            the chain name
	 * @param scr
	 *            the binding source : node[:port]
	 * @param dst
	 *            the binding destination : node[:port]
	 * @throws MetadataException
	 *             the metadata exception
	 */
	public void deleteBinding(Chain chain, String src, String dst) throws MetadataException {
		RepoElement repo = (RepoElement) contentProvider.getParent(chain);
		if (repo == null)
			throw new RuntimeException("Can't delete binding : can't find the underlying RepoElement");
		repo.getDscilia().deleteBinding(chain, src, dst);
	}
}
