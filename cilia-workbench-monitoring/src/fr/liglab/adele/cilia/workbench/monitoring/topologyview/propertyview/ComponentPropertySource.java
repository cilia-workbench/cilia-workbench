package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import fr.liglab.adele.cilia.MediatorReadOnly;


public class ComponentPropertySource extends CommonPropertySource<MediatorReadOnly> implements IPropertySource {

	/* Properties id. */
	private final String PROPERTY_ID = PROPERTY_PREFIX + "id";
	private final String PROPERTY_NAMESPACE = PROPERTY_PREFIX + "namespace";
	private final String PROPERTY_CATEGORY = PROPERTY_PREFIX + "category";
	

	public ComponentPropertySource(MediatorReadOnly modelObject) {
		super(modelObject);
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor id = createBasicPropertyDescriptor(PROPERTY_ID, "id");
		IPropertyDescriptor ns = createBasicPropertyDescriptor(PROPERTY_NAMESPACE, "namespace");
		IPropertyDescriptor cat = createBasicPropertyDescriptor(PROPERTY_CATEGORY, "category");
		return new IPropertyDescriptor[]{id, ns, cat};
	}

	/* (non-Javadoc)
	 * @see fr.liglab.adele.cilia.monitoring.topologyview.propertyview.CommonPropertySource#getBasicPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getBasicPropertyValue(Object id) {
		if (id.equals(PROPERTY_ID))
			return modelObject.getId();
		if (id.equals(PROPERTY_NAMESPACE))
			return modelObject.getNamespace();
		if (id.equals(PROPERTY_CATEGORY))
			return modelObject.getCategory();
				
		// Not found
		return null;
	}
}
