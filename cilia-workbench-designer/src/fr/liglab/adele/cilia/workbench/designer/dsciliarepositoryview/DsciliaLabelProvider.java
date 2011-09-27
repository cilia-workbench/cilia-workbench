package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;


import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Dscilia;

import fr.liglab.adele.cilia.workbench.designer.repositoryview.LabelProvider;

public class DsciliaLabelProvider extends LabelProvider {

	protected String getImagePath(Object obj) {
		String imageName;
		
		if (obj instanceof Dscilia)
			imageName = "icons/16/file.png";
		else if (obj instanceof Chain)
			imageName = "icons/16/chain.png";
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());
		
		return imageName;
	}
}

