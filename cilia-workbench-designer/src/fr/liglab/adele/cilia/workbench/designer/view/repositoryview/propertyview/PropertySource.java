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
package fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Bridge the gap between the model object and the properties view.
 * 
 * @author Etienne Gandrille
 */
public class PropertySource implements IPropertySource {

	/** The model object. */
	private Object modelObject;

	/** The PROPERTIE s_ category. */
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
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> retval = new ArrayList<IPropertyDescriptor>();

		List<Field> fields = new ArrayList<Field>();

		for (Field f : modelObject.getClass().getDeclaredFields())
			fields.add(f);
		try {
			for (Field f : modelObject.getClass().getSuperclass().getDeclaredFields())
				fields.add(f);
		} catch (Exception e) {
			// no super class
		}

		for (Field field : fields) {
			Class<?> type = field.getType();
			if (type.equals(String.class)) {
				String name = field.getName();
				if (isFieldDisplayed(name)) {
					PropertyDescriptor descriptor = new PropertyDescriptor(name, name);
					descriptor.setCategory(PROPERTIES_CATEGORY);
					retval.add(descriptor);
				}
			}
		}

		return retval.toArray(new IPropertyDescriptor[0]);
	}

	/**
	 * Returns true if the field should be displayed according to its name, false otherwise.
	 * 
	 * @param fieldName
	 * @return
	 */
	private boolean isFieldDisplayed(String fieldName) {

		char[] chars = new char[fieldName.length()];
		fieldName.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			boolean nb = (chars[i] >= '0' && chars[i] <= '9');
			// boolean min = (chars[i] >= 'a' && chars[i] <= 'z');
			boolean maj = (chars[i] >= 'A' && chars[i] <= 'Z');
			boolean spec = (chars[i] == '-' || chars[i] == '_' || chars[i] == '.');

			boolean specialCar = (nb || maj || spec);

			if (!specialCar) {
				return true;
			}
		}

		return false;
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
			Field field;
			try {
				field = modelObject.getClass().getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				field = modelObject.getClass().getSuperclass().getDeclaredField(name);
			}
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
