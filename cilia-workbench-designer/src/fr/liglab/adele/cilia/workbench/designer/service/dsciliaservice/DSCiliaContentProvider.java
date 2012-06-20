package fr.liglab.adele.cilia.workbench.designer.service.dsciliaservice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;

public class DSCiliaContentProvider extends GenericContentProvider {

	public DSCiliaContentProvider(List<DSCiliaFile> repo) {

		addRoot(repo);

		for (DSCiliaFile re : repo) {
			addRelationship(true, repo, re);

			if (re.getModel() != null) {
				for (Chain c : re.getModel().getChains()) {
					addRelationship(true, re, c);
				}
			}
		}
	}
}
