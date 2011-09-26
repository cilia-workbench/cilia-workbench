package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.part.ViewPart;

import fr.liglab.adele.cilia.workbench.designer.Activator;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Bundle;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;

public class RepositoryView extends ViewPart {

	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.repositoryview";

	/** Main viewer */
	private TreeViewer viewer;

	/** Message area used to display last model reload date */
	private Label messageArea;
	private final String messageAreaPrefix = "Repository directory: ";

	private Bundle[] model = new Bundle[0];

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
		viewer.setContentProvider(new MetadataContentProvider(model));
		viewer.setLabelProvider(new MetadataLabelProvider());
		viewer.setInput(model);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// viewer.setAutoExpandLevel(2);

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

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				ISelectionService selServ = getSite().getWorkbenchWindow().getSelectionService();
				ISelection sel = selServ.getSelection();
				if (sel != null && sel instanceof TreeSelection) {
					TreeSelection ts = (TreeSelection) sel;
					Object element = ts.getFirstElement();
					if (element instanceof Bundle) {
						Bundle bundle = (Bundle) element;
						String name = bundle.getBundleName();
						IStorage storage = new StreamFromFileStorage(name);
						IStorageEditorInput input = new StringInput(storage);
						
						IWorkbenchPage page = getViewSite().getPage();
						
						try {
							page.openEditor(input, EditorsUI.DEFAULT_TEXT_EDITOR_ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}

					}
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

		File dir = new File(getRepositoryDirectory());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		List<Bundle> bundles = new ArrayList<Bundle>();
		for (File jar : list) {
			try {
				String path = jar.getPath();
				bundles.add(new Bundle(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		model = bundles.toArray(new Bundle[0]);
		viewer.setContentProvider(new MetadataContentProvider(model));
		viewer.setInput(model);
		viewer.refresh();

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
