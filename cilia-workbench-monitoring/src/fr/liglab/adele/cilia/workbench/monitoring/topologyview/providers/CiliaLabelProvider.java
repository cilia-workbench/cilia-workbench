package fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.osgi.framework.Bundle;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.BindingReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;
import fr.liglab.adele.cilia.model.PatternType;
import fr.liglab.adele.cilia.workbench.monitoring.Activator;

/**
 * Label provider for the tree viewer.
 */
public class CiliaLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object obj) {
		if (obj instanceof MonitoredApplication)
			return null;
		if (obj instanceof CiliaContextReadOnly)
			return "Platform";
		if (obj instanceof ChainReadOnly)
			return ((ChainReadOnly) obj).getId();
		if (obj instanceof AdapterReadOnly)
			return ((AdapterReadOnly) obj).getId();
		if (obj instanceof MediatorReadOnly)
			return ((MediatorReadOnly) obj).getId();

		if (obj instanceof EntityConnectionData)
			return "";

		throw new RuntimeException("Unsupported type: " + obj.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		String imageName;
		if (obj instanceof MonitoredApplication)
			imageName = "icons/16/start.png";
		else if (obj instanceof CiliaContextReadOnly)
			imageName = "icons/16/chain.png";
		else if (obj instanceof ChainReadOnly)
			imageName = "icons/16/chain.png";
		else if (obj instanceof AdapterReadOnly) {
			AdapterReadOnly adapter = (AdapterReadOnly) obj;

			// adapter.getPattern().equal(PatternType.IN_ONLY)
			// adapter.getPattern().equal(PatternType.OUT_ONLY)

			int in = adapter.getInBindings() == null ? 0 : adapter.getInBindings().length;
			int out = adapter.getOutBindings() == null ? 0 : adapter.getOutBindings().length;

			if (in == 0 && out != 0)
				imageName = "icons/16/start.png";
			else if (in != 0 && out == 0)
				imageName = "icons/16/stop.png";
			else
				imageName = "icons/16/middle.png";
		} else if (obj instanceof MediatorReadOnly)
			imageName = "icons/16/middle.png";
		else if (obj instanceof EntityConnectionData) {
			imageName = null;
		} else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		if (imageName != null) {
			Bundle bundle = Activator.getDefault().getBundle();
			URL url = FileLocator.find(bundle, new Path(imageName), null);
			try {
				url = new URL("platform:/plugin/fr.liglab.adele.cilia.workbench.common/" + imageName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

			return imageDesc.createImage();
		} else {
			return null;
		}
	}
}
