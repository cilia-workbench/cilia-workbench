package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.workbench.common.Activator;

public abstract class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {

	protected abstract String getImagePath(Object obj);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imageName = getImagePath(obj);

		org.osgi.framework.Bundle bundle = Activator.getDefault().getBundle();
		URL url = FileLocator.find(bundle, new Path(imageName), null);
		try {
			url = new URL("platform:/plugin/fr.liglab.adele.cilia.workbench.common/" + imageName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

		return imageDesc.createImage();
	}
}
