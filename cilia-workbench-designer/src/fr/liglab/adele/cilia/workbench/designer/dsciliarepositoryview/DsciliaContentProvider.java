package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.ContentProvider;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

public class DsciliaContentProvider extends ContentProvider {

	public DsciliaContentProvider(RepoElement[] model) {
		parent.put(model, new Object[0]);
		children.put(model, new ArrayList<Object>());

		for (RepoElement re : model) {
			parent.put(re, model);
			children.get(model).add(re);
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
