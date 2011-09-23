package fr.liglab.adele.cilia.workbench.common.preferencePage;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CiliaRootPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public CiliaRootPreferencePage() {
	}

	public CiliaRootPreferencePage(String title) {
		super(title);
	}

	public CiliaRootPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription("CILIA workbench preferences.");
	}

	@Override
	protected Control createContents(Composite parent) {
		return null;
	}

}
