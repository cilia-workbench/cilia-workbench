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
package fr.liglab.adele.cilia.workbench.common.ui.preferencePage;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * A root page in the preference menu. Should be used by all Cilia Designer sub
 * pages.
 * 
 * @author Etienne Gandrille
 */
public class CiliaRootPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** Message displayed on the top of the page. */
	private static final String PAGE_DESCRITION = "Cilia IDE preferences.";
	public static final String PREFERENCES_FILE_PATH = "preferencesFilePath";

	public static final String pageID = "fr.liglab.adele.cilia.workbench.common.preferencePage.CiliaRootPreferencePage";

	public CiliaRootPreferencePage() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new FileFieldEditor(PREFERENCES_FILE_PATH, "&Preferences file:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getInstance().getPreferenceStore());
		setDescription(PAGE_DESCRITION);
	}
}
