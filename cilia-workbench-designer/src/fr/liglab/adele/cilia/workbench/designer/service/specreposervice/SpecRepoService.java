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
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;

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
		List<SpecFile> files = new ArrayList<SpecFile>();
		for (File file : list) {
			try {
				String path = file.getPath();
				files.add(new SpecFile(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Updates model with computed one
		model = files;

		// Sends notifications
		notifyListeners(null);
	}
}
