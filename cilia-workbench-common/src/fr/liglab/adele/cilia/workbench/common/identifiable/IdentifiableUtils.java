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
package fr.liglab.adele.cilia.workbench.common.identifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;

/**
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
	 * @return an array (which can be null) with errors.
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
}
