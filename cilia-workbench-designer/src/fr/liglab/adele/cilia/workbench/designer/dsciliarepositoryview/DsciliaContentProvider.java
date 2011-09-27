package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Dscilia;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.ContentProvider;

public class DsciliaContentProvider extends ContentProvider {

	public DsciliaContentProvider(Dscilia[] model) {
		parent.put(model, new Object[0]);
		children.put(model, new ArrayList<Object>());

		for (Dscilia dscilia : model) {
			parent.put(dscilia, model);
			children.get(model).add(dscilia);
			children.put(dscilia, new ArrayList<Object>());

			for (Chain c : dscilia.getChains()) {
				parent.put(c, dscilia);
				children.get(dscilia).add(c);
				children.put(c, new ArrayList<Object>());
			}
		}
	}
}
