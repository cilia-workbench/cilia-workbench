package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;


import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.RepositoryView;

public class DsciliaRepositoryView extends RepositoryView {

	/** The Constant viewId. */
	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview";
	
	public DsciliaRepositoryView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//viewer.setLabelProvider(new MetadataLabelProvider());
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Label
		messageArea = new Label(parent, SWT.WRAP);
		messageArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Populates view
		refresh();
		
		// Selection provider
		getSite().setSelectionProvider(viewer);

		// View update on property modification
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() == CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH) {
					refresh();
				}
			}
		});
	}

	@Override
	protected void refresh() {
		super.refresh();
	}

	@Override
	protected String getRepositoryPropertyPath() {
		return CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH;
	}
}
