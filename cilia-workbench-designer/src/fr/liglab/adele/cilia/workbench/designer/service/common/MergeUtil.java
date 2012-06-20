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
package fr.liglab.adele.cilia.workbench.designer.service.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset.Operation;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class MergeUtil {

	public static List<Changeset> computeUpdateChangeset(Object newInstance, Object oldInstance, String... fieldNames)
			throws CiliaException {

		List<Changeset> retval = new ArrayList<Changeset>();

		for (String fieldName : fieldNames) {
			try {
				String newValue = ReflectionUtil.findStringValue(newInstance, fieldName);
				String oldValue = ReflectionUtil.findStringValue(oldInstance, fieldName);

				if (!(oldValue == null && newValue == null)) {
					if (!oldValue.equals(newValue)) {
						ReflectionUtil.setValue(oldInstance, fieldName, newValue);
						Changeset c = new Changeset(Operation.UPDATE, oldInstance);
						retval.add(c);
						return retval;
					}
				}

			} catch (Exception e) {
				throw new CiliaException("Can't compute update changeset. Introspection error.", e);
			}
		}

		return retval;
	}

	public static <Type extends Identifiable> List<Changeset> mergeLists(List<Type> newList, List<Type> oldList)
			throws CiliaException {

		List<Changeset> retval = new ArrayList<Changeset>();

		for (Iterator<Type> itr = oldList.iterator(); itr.hasNext();) {
			Identifiable old = itr.next();
			Object oldID = old.getId();

			Identifiable updated = pullProperty(newList, oldID);
			if (updated == null) {
				itr.remove();
				retval.add(new Changeset(Operation.REMOVE, old));
			} else {
				if (old instanceof Mergeable)
					retval.addAll(((Mergeable) old).merge(updated));
			}
		}

		for (Type object : newList) {
			oldList.add(object);
			retval.add(new Changeset(Operation.ADD, object));
		}

		return retval;
	}

	private static Identifiable pullProperty(Iterable<? extends Identifiable> list, Object id) {
		for (Iterator<? extends Identifiable> itr = list.iterator(); itr.hasNext();) {
			Identifiable element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static List<Changeset> mergeObjectsFields(Object newInstance, Object oldInstance, String fieldName)
			throws CiliaException {

		List<Changeset> retval = new ArrayList<Changeset>();

		try {
			Object newValue = ReflectionUtil.findValue(newInstance, fieldName);
			Object oldValue = ReflectionUtil.findValue(oldInstance, fieldName);

			if (newValue == null && oldValue == null) {
				// nothing to do
			} else if (oldValue != null && newValue == null) {
				retval.add(new Changeset(Operation.REMOVE, oldValue));
				ReflectionUtil.setValue(oldInstance, fieldName, null);
			} else if (oldValue == null && newValue != null) {
				retval.add(new Changeset(Operation.ADD, newValue));
				ReflectionUtil.setValue(oldInstance, fieldName, newValue);
			} else {
				if (oldValue instanceof Mergeable)
					retval.addAll(((Mergeable) oldValue).merge(newValue));
			}
		} catch (Exception e) {
			throw new CiliaException("Error while merging fields objects.", e);
		}

		return retval;
	}
}
