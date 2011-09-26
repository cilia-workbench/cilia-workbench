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

	public static final String REPOSITORY_PATH = "repositoryPath";
	
	
	public CiliaDesignerPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for Cilia Workbench Designer.");
	}
	
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(REPOSITORY_PATH, 
				"&Repository path:", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}