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
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.AbstractRepoService;

/**
 * JarRepoService.
 * 
 * @author Etienne Gandrille
 */
public class JarRepoService extends AbstractRepoService<CiliaJarFile> {

	/** Singleton instance */
	private static JarRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = CiliaDesignerPreferencePage.JAR_REPOSITORY_PATH;

	/** Jar files extension. */
	private final static String ext = ".jar";

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
		super(PREFERENCE_PATH_KEY, ext);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<CiliaJarFile> bundles = new ArrayList<CiliaJarFile>();
		for (File jar : list) {
			String path = jar.getPath();
			bundles.add(new CiliaJarFile(path));
		}

		// Updates model with computed one
		model = bundles;

		// Update content provider
		contentProvider = new JarContentProvider(model);

		// Sends notifications
		notifyListeners(null);
	}

	public String[] getMediatorsId() {
		List<String> retval = new ArrayList<String>();
		for (CiliaJarFile bundle : model)
			for (MediatorComponent mc : bundle.getModel().getMediatorComponents())
				retval.add(mc.getName());

		return retval.toArray(new String[0]);
	}

	public String[] getAdaptersId() {
		List<String> retval = new ArrayList<String>();
		for (CiliaJarFile bundle : model)
			for (Adapter a : bundle.getModel().getAdapters())
				retval.add(a.getName());

		return retval.toArray(new String[0]);
	}

	public Adapter getAdapter(String name) {
		for (CiliaJarFile bundle : model)
			for (Adapter a : bundle.getModel().getAdapters())
				if (a.getName().equalsIgnoreCase(name))
					return a;
		return null;
	}

	public MediatorComponent getMediator(String name) {
		for (CiliaJarFile bundle : model)
			for (MediatorComponent m : bundle.getModel().getMediatorComponents())
				if (m.getName().equalsIgnoreCase(name))
					return m;
		return null;
	}
}
