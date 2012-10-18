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
package fr.liglab.adele.cilia.workbench.restmonitoring.misc.preferencepage;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.liglab.adele.cilia.workbench.common.Activator;

/**
 * 
 * @author Etienne Gandrille
 */
public class RestMonitoringPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String REST_PLATFORM_REPOSITORY_PATH = "restPlatformRepositoryPath";

	public static final String DESCRIPTION = "Preferences for Cilia Workbench Designer.";

	public RestMonitoringPreferencePage() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(REST_PLATFORM_REPOSITORY_PATH, "&Rest platform repository path:",
				getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getInstance().getPreferenceStore());
		setDescription(DESCRIPTION);
	}
}
