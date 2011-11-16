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
package fr.liglab.adele.cilia.workbench.designer.preferencePage;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import fr.liglab.adele.cilia.workbench.designer.Activator;

/**
 * Preferences class for Cilia workbench designer.
 */
public class CiliaDesignerPreferencePage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	/** The Constant REPOSITORY_PATH, used for field identification in the page */
	public static final String JAR_REPOSITORY_PATH = "jarRepositoryPath";
	
	public static final String DSCILIA_REPOSITORY_PATH = "dsciliaRepositoryPath";
	
	/** Page header. */
	public static final String HEADER = "Preferences for Cilia Workbench Designer.";
	
	/**
	 * Instantiates a new Cilia designer preference page.
	 */
	public CiliaDesignerPreferencePage() {
		super(GRID);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(JAR_REPOSITORY_PATH, 
				"&Jar repository path:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(DSCILIA_REPOSITORY_PATH, 
				"&Dscilia repository path:", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(HEADER);
	}
}