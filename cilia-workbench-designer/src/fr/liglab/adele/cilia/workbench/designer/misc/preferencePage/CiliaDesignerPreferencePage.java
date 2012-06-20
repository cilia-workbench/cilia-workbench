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
package fr.liglab.adele.cilia.workbench.designer.misc.preferencePage;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.liglab.adele.cilia.workbench.designer.Activator;

/**
 * Preferences class for Cilia workbench designer.
 * 
 * @author Etienne Gandrille
 */
public class CiliaDesignerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String SPEC_REPOSITORY_PATH = "specRepositoryPath";
	public static final String JAR_REPOSITORY_PATH = "jarRepositoryPath";
	public static final String ABSTRACT_COMPO_REPOSITORY_PATH = "abstractCompoRepositoryPath";
	public static final String CONCRETE_COMPO_REPOSITORY_PATH = "concreteCompoRepositoryPath";

	public static final String DESCRIPTION = "Preferences for Cilia Workbench Designer.";

	public CiliaDesignerPreferencePage() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(SPEC_REPOSITORY_PATH, "&Specification repository path:",
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(JAR_REPOSITORY_PATH, "&Jar repository path:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(ABSTRACT_COMPO_REPOSITORY_PATH, "&Abstract compositions repository path:",
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(CONCRETE_COMPO_REPOSITORY_PATH,
				"&Concrete compositions (DSCilia) repository path:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getInstance().getPreferenceStore());
		setDescription(DESCRIPTION);
	}
}
