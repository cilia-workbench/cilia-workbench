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
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;

/**
 * 
 * @author Etienne Gandrille
 */
public class BindingInstance extends Binding {

	private final Chain chain;

	public BindingInstance(JSONObject binding, Chain chain) throws CiliaException {
		super(getJSONField(binding, "from"), getJSONField(binding, "to"));
		this.chain = chain;
	}

	private static String getJSONField(JSONObject binding, String name) throws CiliaException {
		try {
			return (String) binding.get(name);
		} catch (JSONException e) {
			return ""; // error reported by getErrorsAndWarnings...
		}
	}

	@Override
	protected Chain getChain() {
		return chain;
	}
}
