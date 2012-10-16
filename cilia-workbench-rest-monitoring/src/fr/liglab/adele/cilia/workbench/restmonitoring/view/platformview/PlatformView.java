package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.ui.view.repositoryview.RepositoryView;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;


public class PlatformView extends RepositoryView<PlatformFile, PlatformModel> {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview";

	public PlatformView() {
		super(PlatformRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setLabelProvider(new PlatformLabelProvider());
		
		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openFileEditor(event);
			}
		});

		// Linking already opened editors for getting save notifications
		for (IEditorReference editor : getRelevantFileStoreEditors(PlatformRepoService.ext))
			addEditorSavedListener(editor.getPart(true));
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		for (Changeset change : changes) {
			Object object = change.getObject();
			Operation operation = change.getOperation();
			if (operation != Operation.UPDATE) {
				if (object instanceof PlatformFile) {
					refresh();
					return;
				}
			} else {
				if (object instanceof PlatformFile) {
					refresh();
					return;
				}
			}
		}

		// updates labels and icons
		refreshMessageArea();
		viewer.refresh(true);
	}
}
