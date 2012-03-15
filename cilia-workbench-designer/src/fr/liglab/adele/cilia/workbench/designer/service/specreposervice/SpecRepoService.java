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
package fr.liglab.adele.cilia.workbench.designer.service.specreposervice;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.PullElementUtil;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.Changeset.Operation;

public class SpecRepoService extends AbstractRepoService<SpecFile> {

	/** Singleton instance */
	private static SpecRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.SPEC_REPOSITORY_PATH;

	/** Spec files extension. */
	private final static String ext = ".xml";

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the instance.
	 */
	public static SpecRepoService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SpecRepoService();
		return INSTANCE;
	}

	/**
	 * Instantiates a new jar repo service.
	 */
	private SpecRepoService() {
		super(PREFERENCE_PATH_KEY, ext);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<SpecFile> elements = new ArrayList<SpecFile>();
		for (File file : list) {
			String path = file.getPath();
			elements.add(new SpecFile(path));
		}

		// Updates existing model with computed model
		Changeset[] changes = merge(elements);

		// Update content provider
		contentProvider = new SpecContentProvider(model);
		
		// Sends notifications
		notifyListeners(null);
	}
	
	/**
	 * Merge a list of repo element into the current model. Only differences
	 * between the argument and the model are merge back into the model.
	 * 
	 * @param repoElements
	 *            a new model
	 * @return a list of changesets, which can be empty.
	 */
	private Changeset[] merge(List<SpecFile> repoElements) {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<SpecFile> itr = model.iterator(); itr.hasNext();) {
			SpecFile old = itr.next();
			String id = old.getFilePath();
			SpecFile updated = PullElementUtil.pullRepoElement(repoElements, id);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				for (Changeset c : old.merge(updated))
					retval.add(c);
			}
		}

		for (SpecFile r : repoElements) {
			model.add(r);
			retval.add(new Changeset(Operation.ADD, r));
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval.toArray(new Changeset[0]);
	}
}
