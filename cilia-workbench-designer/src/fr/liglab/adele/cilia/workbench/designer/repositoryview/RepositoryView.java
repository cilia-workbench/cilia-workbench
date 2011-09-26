package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;

public class RepositoryView extends ViewPart {

	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.repositoryview";

	/** Main viewer */
	private TreeViewer viewer;

	/** Message area used to display last model reload date */
	private Label messageArea;
	private final String messageAreaPrefix = "Repository directory: ";

	
	public RepositoryView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//viewer.setContentProvider(new TreeContentProvider(this));
		//viewer.setLabelProvider(new CiliaLabelProvider());
		//viewer.setInput(model);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//viewer.setAutoExpandLevel(2);

		// Label
		messageArea = new Label(parent, SWT.WRAP);
		messageArea.setText(getMessageAreaText());
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Selection provider
		getSite().setSelectionProvider(viewer);
		
		// View update on property modification
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() == CiliaDesignerPreferencePage.REPOSITORY_PATH) {
					reload();
				}
			}
		});
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void reload() {
		messageArea.setText(getMessageAreaText());
		System.out.println("Reload");
	}
	
	private String getRepositoryDirectory() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getString(CiliaDesignerPreferencePage.REPOSITORY_PATH);		
	}
	
	private String getMessageAreaText() {
		String dir = getRepositoryDirectory();
		if (dir == null || dir.length() == 0)
			return messageAreaPrefix + "not available";
		else
			return messageAreaPrefix + dir;
	}
}
