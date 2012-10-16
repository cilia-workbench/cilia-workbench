package fr.liglab.adele.cilia.workbench.restmonitoring.service.platform;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;

public class PlatformContentProvider  extends GenericContentProvider {

	public PlatformContentProvider(List<PlatformFile> root) {
	
		addRoot(root);

		for (PlatformFile file : root)
			addRelationship(true, root, file);
	}
}
