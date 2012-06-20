package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaModel;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.common.RepositoryView;

public class DSCiliaRepositoryView extends RepositoryView<DSCiliaFile, DSCiliaModel> {

	public final static String VIEW_ID = "fr.liglab.adele.cilia.workbench.designer.view.dsciliaview";

	public DSCiliaRepositoryView() {
		super(DSCiliaRepoService.getInstance());
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setLabelProvider(new DSCiliaLabelProvider());

		// TreeViewer listener
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openFileEditor(event);
			}
		});

		// Linking already opened editors for getting save notifications
		for (IEditorReference editor : getRelevantFileStoreEditors(DSCiliaRepoService.ext))
			addEditorSavedListener(editor.getPart(true));
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		for (Changeset change : changes) {
			Object object = change.getObject();
			Operation operation = change.getOperation();
			if (operation != Operation.UPDATE) {
				if (object instanceof DSCiliaFile || object instanceof Chain) {
					refresh();
					return;
				}
			} else {
				if (object instanceof DSCiliaFile) {
					refresh();
					return;
				}
			}
		}

		// updates labels and icons
		viewer.refresh(true);
	}
}