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

public abstract class RepositoryView extends ViewPart {

	/** Main viewer. */
	protected TreeViewer viewer;

	/** Message area used to display last model reload date. */
	protected Label messageArea;

	/** The message area prefix. */
	protected final String messageAreaPrefix = "Repository directory: ";
	
	public void createPartControl(Composite parent) {
		// Global layout
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		// Viewer
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
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
				if (event.getProperty() == getRepositoryPropertyPath()) {
					refresh();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	/**
	 * Gets the repository directory.
	 * 
	 * @return the repository directory
	 */
	protected String getRepositoryDirectory() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getString(getRepositoryPropertyPath());
	}

	/**
	 * Computes the message area text.
	 * 
	 * @return the message area text
	 */
	protected String computeMessageAreaText() {
		String dir = getRepositoryDirectory();
		if (dir == null || dir.length() == 0)
			return messageAreaPrefix + "not available";
		else
			return messageAreaPrefix + dir;
	}
	
	protected void refresh() {
		messageArea.setText(computeMessageAreaText());
	}
	
	protected abstract String getRepositoryPropertyPath();
}
