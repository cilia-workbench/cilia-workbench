package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

public class PlatformLabelProvider extends CiliaLabelProvider {

	@Override
	protected GenericContentProvider getContentProvider() {
		return PlatformRepoService.getInstance().getContentProvider();
	}

	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (isCompatible(obj, PlatformFile.class))
			imageName = ImageDescriptorEnum.FILE;

		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
