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
package fr.liglab.adele.cilia.workbench.common.reflection;

import java.lang.reflect.Field;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * A few methods, for reading XML and injecting values into objects fields.
 * 
 * @author Etienne Gandrille
 */
public class ReflectionUtil {

	public static boolean setAttribute(Node node, String attrName, Object object, String fieldName)
			throws CiliaException {
		return setAttribute(node, attrName, object, fieldName, null);
	}

	public static boolean setAttribute(Node node, String attrName, Object object, String fieldName, String defaultValue)
			throws CiliaException {
		return setAttributeInternal(false, defaultValue, node, attrName, object, fieldName);
	}

	private static boolean setAttributeInternal(boolean requiredAttribute, String defaultValue, Node node,
			String attrName, Object object, String fieldName) throws CiliaException {

		Exception exception = null;
		boolean retval = true;
		try {
			Field field = findField(object, fieldName);

			// finds value
			String value = null;
			try {
				value = XMLHelpers.findAttributeValue(node, attrName);
			} catch (CiliaException e) {
				retval = false;
				if (requiredAttribute == true)
					throw new CiliaException(e);
				else if (defaultValue != null)
					value = defaultValue;
			}

			// set value
			field.setAccessible(true);
			field.set(object, value);

		} catch (SecurityException e) {
			exception = e;
		} catch (NoSuchFieldException e) {
			exception = e;
		} catch (IllegalArgumentException e) {
			exception = e;
		} catch (IllegalAccessException e) {
			exception = e;
		}

		// If there's an error
		if (exception != null)
			throw new CiliaException("", exception);

		// Success
		return retval;
	}

	private static Field findField(Object object, String fieldName) throws NoSuchFieldException {
		Field field = null;
		Class<? extends Object> claz = object.getClass();
		while (claz != null && field == null) {
			try {
				field = claz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				claz = claz.getSuperclass();
			}
		}
		if (field == null)
			throw new NoSuchFieldException("Can't find field with name " + fieldName);

		return field;
	}

	public static String findStringValue(Object object, String fieldName) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = findField(object, fieldName);
		field.setAccessible(true);
		return (String) field.get(object);
	}

	public static Object findValue(Object object, String fieldName) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = findField(object, fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	public static void setValue(Object object, String fieldName, Object value) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = findField(object, fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}
}
