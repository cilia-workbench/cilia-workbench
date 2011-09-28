package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Dscilia;
import fr.liglab.adele.cilia.workbench.designer.preferencePage.CiliaDesignerPreferencePage;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.RepositoryView;

public class DsciliaRepositoryView extends RepositoryView {

	/** The Constant viewId. */
	public final static String viewId = "fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview";
	private Dscilia[] model = new Dscilia[0];

	public DsciliaRepositoryView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		viewer.setLabelProvider(new DsciliaLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openEditor(event);
			}
		});
	}

	private void openEditor(DoubleClickEvent event) {
		Object element = getFirstSelectedElement();

		if (element != null && element instanceof Dscilia) {
			Dscilia dscilia = (Dscilia) element;
			IFileStore fileStore;
			try {
				fileStore = EFS.getLocalFileSystem().getStore(new URI(dscilia.getFilePath()));
				IWorkbenchPage page = getViewSite().getPage();
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void refresh() {
		super.refresh();

		File dir = new File(getRepositoryDirectory());
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".dscilia");
			}
		});

		List<Dscilia> dscilias = new ArrayList<Dscilia>();
		if (list != null) {
			for (File jar : list) {
				try {
					String path = jar.getPath();
					dscilias.add(new Dscilia(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		model = dscilias.toArray(new Dscilia[0]);
		viewer.setContentProvider(new DsciliaContentProvider(model));
		viewer.setInput(model);
		viewer.refresh();
	}

	@Override
	protected String getRepositoryPropertyPath() {
		return CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH;
	}
}
