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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChain extends Chain {

	private final PlatformModel platform;

	public PlatformChain(String name, PlatformModel platform) {
		super(name);
		this.platform = platform;
	}

	public PlatformChain(JSONObject json, PlatformModel platform) throws CiliaException {
		super(getJSONname(json));

		this.platform = platform;

		try {
			JSONArray mediatorsList = json.getJSONArray("Mediators");
			for (int i = 0; i < mediatorsList.length(); i++) {
				String mediatorName = (String) mediatorsList.get(i);
				// TODO compute component type
				NameNamespaceID mediatorTypeID = null; // here !
				mediators.add(new MediatorInstanceRef(mediatorName, mediatorTypeID, this));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing mediators list", e);
		}

		try {
			JSONObject adaptersRoot = json.getJSONObject("Adapters");

			JSONArray inAdaptersList = adaptersRoot.getJSONArray("in-only");
			for (int i = 0; i < inAdaptersList.length(); i++) {
				String adapterName = (String) inAdaptersList.get(i);
				// TODO compute component type
				NameNamespaceID adapterTypeID = null; // here !
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

			JSONArray outAdaptersList = adaptersRoot.getJSONArray("out-only");
			for (int i = 0; i < outAdaptersList.length(); i++) {
				String adapterName = (String) outAdaptersList.get(i);
				// TODO compute component type
				NameNamespaceID adapterTypeID = null; // here !
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

			JSONArray inOutAdaptersList = adaptersRoot.getJSONArray("in-out");
			for (int i = 0; i < inOutAdaptersList.length(); i++) {
				String adapterName = (String) outAdaptersList.get(i);
				// TODO compute component type
				NameNamespaceID adapterTypeID = null; // here !
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}

		try {
			JSONArray bindingsList = json.getJSONArray("Bindings");
			for (int i = 0; i < bindingsList.length(); i++) {
				JSONObject binding = (JSONObject) bindingsList.get(i);
				bindings.add(new BindingInstance(binding, this));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}
	}

	private static String getJSONname(JSONObject json) {
		try {
			return json.getString("Chain");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getId() {
		return getName();
	}

	public PlatformModel getPlatform() {
		return platform;
	}
}
