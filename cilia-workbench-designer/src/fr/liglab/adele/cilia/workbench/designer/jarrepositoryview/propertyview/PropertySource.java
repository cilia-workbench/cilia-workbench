/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview.propertyview;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Bridge the gap between the Chain model object (of type ChainReadOnly) and the properties view.
 * 
 * @author Etienne Gandrille
 */
public class PropertySource implements IPropertySource {

	/** The model object. */
	private Object modelObject;

	/** The name for the default category */
	protected final String PROPERTIES_CATEGORY = "Basic properties";

	/**
	 * Instantiates a new property source.
	 * 
	 * @param modelObject
	 *            the model object
	 */
	public PropertySource(Object modelObject) {
		this.modelObject = modelObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		Field[] fields = modelObject.getClass().getDeclaredFields();

		List<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		for (Field field : fields) {
			Class<?> type = field.getType();
			if (type.equals(String.class)) {
				String name = field.getName();
				PropertyDescriptor descriptor = new PropertyDescriptor(name, name);
				descriptor.setCategory(PROPERTIES_CATEGORY);
				list.add(descriptor);
			}
		}

		return list.toArray(new IPropertyDescriptor[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		String name = (String) id;
		try {
			Field field = modelObject.getClass().getDeclaredField(name);
			field.setAccessible(true);
			Object value = field.get(modelObject);
			return value;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	@Override
	public void resetPropertyValue(Object id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
	}
}
