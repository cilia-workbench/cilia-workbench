package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import fr.liglab.adele.cilia.workbench.common.sourceprovider.ToggleSourceProvider;

public class VariablesSourceProvider extends ToggleSourceProvider {

	public final static String VARIABLE_NAME = "fr.liglab.adele.cilia.workbench.designer.chaindesignerview.toolbarEnable";
	private final static String TOOLBAR_ENABLE = "enable";
	private final static String TOOLBAR_DISABLE = "disable";
	private final static boolean defaultValue = false;

	public VariablesSourceProvider() {
		super(VARIABLE_NAME, TOOLBAR_ENABLE, TOOLBAR_DISABLE, defaultValue);
	}
}
