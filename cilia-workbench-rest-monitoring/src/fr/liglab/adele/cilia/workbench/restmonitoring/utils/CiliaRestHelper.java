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
package fr.liglab.adele.cilia.workbench.restmonitoring.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;

/**
 * 
 * @author Etienne Gandrille
 */
public class CiliaRestHelper {

	public static String[] getChainsList(PlatformID platformID) throws CiliaException {

		HttpResquestResult response = get(platformID, "/cilia");

		String json = response.getMessage();
		List<String> retval = new ArrayList<String>();

		try {
			JSONObject js = new JSONObject(json);
			JSONArray chains = js.getJSONArray("chains");

			for (int i = 0; i < chains.length(); i++)
				retval.add(chains.getString(i));
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}

		return retval.toArray(new String[0]);
	}

	public static JSONObject getChainContent(PlatformID platformID, String chainName) throws CiliaException {

		HttpResquestResult response = get(platformID, "/cilia" + "/" + chainName);

		String json = response.getMessage();

		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}
	}

	private static JSONObject getComponentContent(PlatformID platformID, String chainName, String componentTypeName, String componentName)
			throws CiliaException {
		HttpResquestResult response = get(platformID, "/cilia" + "/" + chainName + "/" + componentTypeName + "/" + componentName);

		String json = response.getMessage();

		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}
	}

	public static JSONObject getMediatorContent(PlatformID platformID, String chainName, String mediatorName) throws CiliaException {
		return getComponentContent(platformID, chainName, "mediators", mediatorName);
	}

	public static JSONObject getAdapterContent(PlatformID platformID, String chainName, String adapterName) throws CiliaException {
		return getComponentContent(platformID, chainName, "adapters", adapterName);
	}

	private static Map<String, String> getComponentPropertiesFromJSON(JSONObject json) throws CiliaException {
		Map<String, String> retval = new HashMap<String, String>();
		try {
			JSONObject prop = (JSONObject) json.get("Properties");
			String[] keys = JSONObject.getNames(prop);
			for (String key : keys) {
				String value = (String) prop.get(key);
				retval.put(key, value);
			}
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}

		return retval;
	}

	public static Map<String, String> getAdapterProperties(PlatformID platformID, String chainName, String adapterName) throws CiliaException {
		JSONObject json = getAdapterContent(platformID, chainName, adapterName);
		return getComponentPropertiesFromJSON(json);
	}

	public static Map<String, String> getMediatorProperties(PlatformID platformID, String chainName, String adapterName) throws CiliaException {
		JSONObject json = getMediatorContent(platformID, chainName, adapterName);
		return getComponentPropertiesFromJSON(json);
	}

	private static Map<String, String> getComponentInformationFromJSON(JSONObject json) throws CiliaException {
		Map<String, String> retval = new HashMap<String, String>();
		try {
			String[] keys = JSONObject.getNames(json);
			for (String key : keys) {
				if (!key.equalsIgnoreCase("Properties")) {
					String value = json.getString(key);
					retval.put(key, value);
				}
			}
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}

		return retval;
	}

	public static Map<String, String> getAdapterInformation(PlatformID platformID, String chainName, String adapterName) throws CiliaException {
		JSONObject json = getAdapterContent(platformID, chainName, adapterName);
		return getComponentInformationFromJSON(json);
	}

	public static Map<String, String> getMediatorInformation(PlatformID platformID, String chainName, String mediatorName) throws CiliaException {
		JSONObject json = getMediatorContent(platformID, chainName, mediatorName);
		return getComponentInformationFromJSON(json);
	}

	private static HttpResquestResult get(PlatformID platformID, String url) throws CiliaException {
		HttpHelper http = new HttpHelper(platformID);
		HttpResquestResult result = null;

		try {
			result = http.get(url);
		} catch (CiliaException e) {
			String message = "Can't join " + platformID.toString();
			throw new CiliaException(message, e);
		}

		try {
			http.close();
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		if (result.getStatus().getStatusCode() != 200) {
			String message = "HTTP GET fail (" + result.getStatus().getStatusCode() + ") on resource " + url;
			throw new CiliaException(message);
		}

		return result;
	}
}