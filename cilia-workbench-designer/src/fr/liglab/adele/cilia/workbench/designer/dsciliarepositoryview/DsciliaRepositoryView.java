package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

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

		model  = dscilias.toArray(new Dscilia[0]);		
		viewer.setContentProvider(new DsciliaContentProvider(model));
		viewer.setInput(model);
		viewer.refresh();
	}

	@Override
	protected String getRepositoryPropertyPath() {
		return CiliaDesignerPreferencePage.DSCILIA_REPOSITORY_PATH;
	}
}
