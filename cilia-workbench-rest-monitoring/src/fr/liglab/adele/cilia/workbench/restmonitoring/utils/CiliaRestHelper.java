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
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * 
 * @author Etienne Gandrille
 */
public class CiliaRestHelper {

	public static String[] getChainsList(String hostname, int port) throws CiliaException {

		HttpResquestResult response = get(hostname, port, "/cilia");

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

	public static String[] getChainContent(String hostname, int port, String chainName) throws CiliaException {

		HttpResquestResult response = get(hostname, port, "/cilia" + "/" + chainName);

		String json = response.getMessage();

		try {
			JSONObject js = new JSONObject(json);

			System.out.println(js.getString("Chain") + "\n");
			JSONArray array = js.getJSONArray("Mediators");

			for (int i = 0; i < array.length(); i++)
				System.out.println(array.getString(i) + "\n");

		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}

		// TODO implementation !

		return null;
	}

	private static HttpResquestResult get(String hostname, int port, String url) throws CiliaException {
		HTTPhelper http = new HTTPhelper(hostname, port);
		HttpResquestResult result = null;

		try {
			result = http.get(url);
		} catch (CiliaException e) {
			String message = "Can't join " + hostname + ":" + port;
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