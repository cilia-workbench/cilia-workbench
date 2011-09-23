package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import fr.liglab.adele.cilia.CiliaContextReadOnly;

public class CiliaContextPropertySource extends CommonPropertySource<CiliaContextReadOnly> implements IPropertySource {

	/* Properties id. */
	protected final String PROPERTY_VERSION = PROPERTY_PREFIX + "version";

	/**
	 * Constructor.
	 * 
	 * @param adaptableObject
	 *            the adaptable object
	 */
	public CiliaContextPropertySource(CiliaContextReadOnly adaptableObject) {
		super(adaptableObject);
	}

	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor version = createBasicPropertyDescriptor(PROPERTY_VERSION, "cilia version");
		return new IPropertyDescriptor[]{version};
	}

	@Override
	public Object getBasicPropertyValue(Object id) {

		if (id.equals(PROPERTY_VERSION))
			return modelObject.getCiliaVersion();

		// Not found
		return null;
	}
}
