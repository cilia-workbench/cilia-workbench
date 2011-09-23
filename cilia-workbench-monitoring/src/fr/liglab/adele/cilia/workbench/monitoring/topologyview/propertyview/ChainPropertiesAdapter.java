package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import fr.liglab.adele.cilia.ChainReadOnly;

public class ChainPropertiesAdapter implements IAdapterFactory {

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class)
			return new ChainPropertySource((ChainReadOnly) adaptableObject);
		return null;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class};
	}

}
