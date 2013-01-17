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
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.CiliaRestHelper;

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
				NameNamespaceID mediatorTypeID = getMediatorTypeID(platform, getName(), mediatorName);
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
				NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

			JSONArray outAdaptersList = adaptersRoot.getJSONArray("out-only");
			for (int i = 0; i < outAdaptersList.length(); i++) {
				String adapterName = (String) outAdaptersList.get(i);
				NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

			JSONArray inOutAdaptersList = adaptersRoot.getJSONArray("in-out");
			for (int i = 0; i < inOutAdaptersList.length(); i++) {
				String adapterName = (String) inOutAdaptersList.get(i);
				NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);
				adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, this));
			}

			JSONArray unknownAdaptersList = adaptersRoot.getJSONArray("unknown");
			for (int i = 0; i < unknownAdaptersList.length(); i++) {
				String adapterName = (String) unknownAdaptersList.get(i);
				NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);
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
			return json.getString("ID");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static NameNamespaceID getAdapterTypeID(PlatformModel platform, String chainName, String adapterName) throws CiliaException {
		try {
			JSONObject adapter = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), chainName, adapterName);
			String type = adapter.getString("Type");
			String namespace = adapter.getString("Namespace");
			return new NameNamespaceID(type, namespace);
		} catch (JSONException e) {
			throw new CiliaException(e);
		}
	}

	private static NameNamespaceID getMediatorTypeID(PlatformModel platform, String chainName, String mediatorName) throws CiliaException {
		try {
			JSONObject adapter = CiliaRestHelper.getMediatorContent(platform.getPlatformID(), chainName, mediatorName);
			String type = adapter.getString("Type");
			String namespace = adapter.getString("Namespace");
			return new NameNamespaceID(type, namespace);
		} catch (JSONException e) {
			throw new CiliaException(e);
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
