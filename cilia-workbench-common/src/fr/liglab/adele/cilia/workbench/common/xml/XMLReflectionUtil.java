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
package fr.liglab.adele.cilia.workbench.common.xml;

import java.lang.reflect.Field;

import org.w3c.dom.Node;

/**
 * A few methods, for reading XML and injecting values into fields.
 * 
 * @author Etienne Gandrille
 */
public class XMLReflectionUtil {

	public static boolean setAttribute(Node node, String attrName, Object object, String fieldName)
			throws MetadataException {
		return setAttribute(node, attrName, object, fieldName, null);
	}

	public static boolean setAttribute(Node node, String attrName, Object object, String fieldName,
			String defaultValue) throws MetadataException {
		return setAttributeInternal(false, defaultValue, node, attrName, object, fieldName);
	}

	private static boolean setAttributeInternal(boolean requiredAttribute, String defaultValue, Node node,
			String attrName, Object object, String fieldName) throws MetadataException {

		Exception exception = null;
		boolean retval = true;
		try {
			Field field;
			try {
				field = object.getClass().getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				field = object.getClass().getSuperclass().getDeclaredField(fieldName);
			}
			String value = null;

			try {
				value = XMLHelpers.findAttributeValue(node, attrName);
			} catch (MetadataException e) {
				retval = false;
				if (requiredAttribute == true)
					throw new MetadataException(e);
				else if (defaultValue != null)
					value = defaultValue;
			}

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
			throw new MetadataException("", exception);

		// Success
		return retval;
	}
}
