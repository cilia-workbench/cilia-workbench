package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import fr.liglab.adele.cilia.ChainReadOnly;

/**
 * Bridge the gap between the Chain model object (of type ChainReadOnly) and the
 * properties view.
 */
public class ChainPropertySource extends CommonPropertySource<ChainReadOnly> implements IPropertySource {

	/* Properties id. */
	private final String PROPERTY_ID = PROPERTY_PREFIX + "id";

	public ChainPropertySource(ChainReadOnly modelObject) {
		super(modelObject);
	}

	@Override
	public IPropertyDescriptor[] getBasicPropertyDescriptors() {
		IPropertyDescriptor id = createBasicPropertyDescriptor(PROPERTY_ID, "chainID");
		return new IPropertyDescriptor[]{id};
	}

	@Override
	public Object getBasicPropertyValue(Object id) {
		if (id.equals(PROPERTY_ID)) {
			return modelObject.getId();
		}
		
		return null;
	}
}
