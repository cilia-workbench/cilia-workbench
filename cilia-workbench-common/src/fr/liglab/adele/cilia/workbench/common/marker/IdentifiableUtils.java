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
package fr.liglab.adele.cilia.workbench.common.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;

/**
 * Helpers methods, for managing errors from {@link Identifiable} objects.
 * 
 * @author Etienne Gandrille
 */
public abstract class IdentifiableUtils {

	/**
	 * Checks if the array does not contains multiple objects with the same id.
	 * 
	 * @param sourceProvider
	 *            the source provider, for error generation
	 * @param data
	 * @return an array (which can be null) with errors if two objects have the
	 *         same id from the {@link Identifiable} point of view.
	 */
	public static List<CiliaFlag> getErrorsNonUniqueId(Object sourceProvider, Iterable<? extends Identifiable> data) {
		Map<Object, Integer> counter = new HashMap<Object, Integer>();

		for (Identifiable item : data) {
			Object id = item.getId();
			if (counter.containsKey(id)) {
				int newVal = counter.remove(id) + 1;
				counter.put(id, newVal);
			} else {
				counter.put(id, 1);
			}
		}

		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();
		for (Object key : counter.keySet()) {
			Integer nb = counter.get(key);
			if (nb > 1) {
				String part = (nb == 2) ? "twice" : nb + " times";
				retval.add(new CiliaError(key + " is not unique (defined " + part + ")", sourceProvider));
			}
		}

		return retval;
	}

	/**
	 * Checks if two lists contains the same objects, form the identifiable
	 * point of view.
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean isSameListId(Iterable<? extends Identifiable> list1, Iterable<? extends Identifiable> list2) {
		List<Object> l1 = new ArrayList<Object>();
		List<Object> l2 = new ArrayList<Object>();

		for (Identifiable item : list1)
			l1.add(item.getId());
		for (Identifiable item : list2)
			l2.add(item.getId());

		if (l1.size() != l2.size())
			return false;

		return (l1.containsAll(l2) && l2.containsAll(l1));
	}
}
