package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.dsciliaview;

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.dscilia.DSCiliaFile;
import fr.liglab.adele.cilia.workbench.designer.service.chain.dsciliaservice.DSCiliaRepoService;

public class DSCiliaLabelProvider extends CiliaLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return DSCiliaRepoService.getInstance().getContentProvider();
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (isCompatible(obj, DSCiliaFile.class))
			imageName = ImageDescriptorEnum.FILE;
		else if (isCompatible(obj, Chain.class))
			imageName = ImageDescriptorEnum.CHAIN;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}