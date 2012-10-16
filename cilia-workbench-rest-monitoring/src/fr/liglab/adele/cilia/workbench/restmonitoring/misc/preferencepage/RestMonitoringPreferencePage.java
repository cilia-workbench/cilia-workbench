package fr.liglab.adele.cilia.workbench.restmonitoring.misc.preferencepage;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.liglab.adele.cilia.workbench.common.Activator;

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
