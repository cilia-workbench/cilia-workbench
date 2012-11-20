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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;

/**
 * Bridge the gap between the model object and the properties view.
 * 
 * @author Etienne Gandrille
 */
public class PropertySource implements IPropertySource {

	private Object modelObject;
	private final String PROPERTIES_CATEGORY = "Basic properties";

	public PropertySource(Object modelObject) {

		while (modelObject instanceof DisplayedInPropertiesViewWithForward
				&& modelObject != ((DisplayedInPropertiesViewWithForward) modelObject).getObjectForComputingProperties())
			modelObject = ((DisplayedInPropertiesViewWithForward) modelObject).getObjectForComputingProperties();

		this.modelObject = modelObject;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> retval = new ArrayList<IPropertyDescriptor>();

		for (Field field : ReflectionUtil.getFields(modelObject)) {
			Class<?> type = field.getType();
			if (type.equals(String.class) || type.equals(NameNamespaceID.class)) {
				String name = field.getName();

				if (isFieldDisplayed(name)) {

					if (type.equals(String.class)) {
						FieldID fieldID = new FieldID(name, null, type);
						PropertyDescriptor descriptor = new PropertyDescriptor(fieldID, name.replaceAll("_", " "));
						descriptor.setCategory(PROPERTIES_CATEGORY);
						retval.add(descriptor);

					} else if (type.equals(NameNamespaceID.class)) {
						// name
						FieldID fieldID1 = new FieldID(name, "name", type);
						PropertyDescriptor descriptor1 = new PropertyDescriptor(fieldID1, name.replaceAll("_", " ") + ":name");
						descriptor1.setCategory(PROPERTIES_CATEGORY);
						retval.add(descriptor1);

						// namespace
						FieldID fieldID2 = new FieldID(name, "namespace", type);
						PropertyDescriptor descriptor2 = new PropertyDescriptor(fieldID2, name.replaceAll("_", " ") + ":namespace");
						descriptor2.setCategory(PROPERTIES_CATEGORY);
						retval.add(descriptor2);
					}
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
		FieldID fieldID = (FieldID) id;
		String name = fieldID.getName();

		try {
			// finds field
			Field field = ReflectionUtil.findField(modelObject, name);
			field.setAccessible(true);

			// getting value
			if (fieldID.getType().equals(String.class)) {
				return field.get(modelObject);
			} else if (fieldID.getType().equals(NameNamespaceID.class)) {
				NameNamespaceID value = (NameNamespaceID) field.get(modelObject);
				if (fieldID.getSubName().equals("name"))
					return value.getName();
				else
					return value.getNamespace();
			}
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

	private class FieldID {

		private final String fieldName;
		private final String subFieldName;
		private final Class<?> fieldType;

		public FieldID(String fieldName, String subFieldName, Class<?> fieldType) {
			this.fieldName = fieldName;
			this.subFieldName = subFieldName;
			this.fieldType = fieldType;
		}

		public String getName() {
			return fieldName;
		}

		public String getSubName() {
			return subFieldName;
		}

		public Class<?> getType() {
			return fieldType;
		}
	}
}
