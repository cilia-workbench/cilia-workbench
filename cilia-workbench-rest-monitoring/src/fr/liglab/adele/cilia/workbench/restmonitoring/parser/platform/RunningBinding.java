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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.parser.Binding;

/**
 * 
 * @author Etienne Gandrille
 */
public class RunningBinding extends Binding {

	public RunningBinding(JSONObject binding) throws CiliaException {
		super(getJSONField(binding, "from"), getJSONField(binding, "to"));
	}

	private static String getJSONField(JSONObject binding, String name) throws CiliaException {
		try {
			return (String) binding.get(name);
		} catch (JSONException e) {
			throw new CiliaException("Error while pasing binding field " + name, e);
		}
	}
}
