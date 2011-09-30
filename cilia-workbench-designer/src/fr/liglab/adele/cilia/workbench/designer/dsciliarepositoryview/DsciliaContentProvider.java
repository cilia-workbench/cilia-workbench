package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.ContentProvider;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

public class DsciliaContentProvider extends ContentProvider {

	public DsciliaContentProvider(List<RepoElement> repo) {
		parent.put(repo, new Object[0]);
		children.put(repo, new ArrayList<Object>());

		for (RepoElement re : repo) {
			parent.put(re, repo);
			children.get(repo).add(re);
			children.put(re, new ArrayList<Object>());

			if (re.getDscilia() != null) {
				for (Chain c : re.getDscilia().getChains()) {
					parent.put(c, re);
					children.get(re).add(c);
					children.put(c, new ArrayList<Object>());
				}
			}
		}
	}
}
