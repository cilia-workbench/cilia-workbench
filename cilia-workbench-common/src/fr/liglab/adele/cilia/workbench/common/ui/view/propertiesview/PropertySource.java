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
package fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview;

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

	private Object modelObject;
	private final String PROPERTIES_CATEGORY = "Basic properties";

	public PropertySource(Object modelObject) {
		this.modelObject = modelObject;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> retval = new ArrayList<IPropertyDescriptor>();

		List<Field> fields = new ArrayList<Field>();

		Class<? extends Object> claz = modelObject.getClass();
		while (claz != null) {
			for (Field f : claz.getDeclaredFields())
				fields.add(f);
			claz = claz.getSuperclass();
		}

		for (Field field : fields) {
			Class<?> type = field.getType();
			if (type.equals(String.class)) {
				String name = field.getName();
				if (isFieldDisplayed(name)) {
					PropertyDescriptor descriptor = new PropertyDescriptor(name, name.replaceAll("_", " "));
					descriptor.setCategory(PROPERTIES_CATEGORY);
					retval.add(descriptor);
				}
			}
		}

		return retval.toArray(new IPropertyDescriptor[0]);
	}

	/**
	 * Returns true if the field should be displayed according to its name,
	 * false otherwise.
	 * 
	 * @param fieldName
	 * @return
	 */
	private boolean isFieldDisplayed(String fieldName) {

		char[] chars = new char[fieldName.length()];
		fieldName.getChars(0, chars.length, chars, 0);

		// names beginning with '_' should NOT been displayed
		if (chars[0] == '_')
			return false;

		// names with ONLY special char should NOT been displayed
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

	@Override
	public Object getPropertyValue(Object id) {
		String name = (String) id;
		try {
			Field field = null;

			// finds field
			Class<? extends Object> claz = modelObject.getClass();
			while (claz != null && field == null) {
				try {
					field = claz.getDeclaredField(name);
				} catch (NoSuchFieldException e) {
					claz = claz.getSuperclass();
				}
			}

			// field not found
			if (field == null)
				throw new NoSuchFieldException("field " + name + " not found in class hierarchy.");

			// getting field
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

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}
}