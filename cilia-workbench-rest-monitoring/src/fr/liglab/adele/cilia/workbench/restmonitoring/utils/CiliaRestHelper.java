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
		
		HTTPhelper http = new HTTPhelper(hostname, port);
		HttpResquestResult response = null;
		try {
			response = http.get("/cilia");
		} catch (CiliaException e) {
			String message = "Can't join " + hostname + ":" + port;
			throw new CiliaException(message, e);
		}
		try {
			http.close();
		} catch (CiliaException e) {
			e.printStackTrace();
		}
	
		if (response.getStatus().getStatusCode() != 200) {
			String message = "Can't get list on server. Status code: " + response.getStatus().getStatusCode();
			throw new CiliaException(message);
		}
		
		String json = response.getMessage();
		List<String> retval = new ArrayList<String>(); 

		try {
			JSONObject js = new JSONObject(json);
			JSONArray chains = js.getJSONArray("chains");
			
			for (int i=0; i<chains.length(); i++)
				retval.add(chains.getString(i));
		} catch (JSONException e) {
			String message = "Error while parsing JSON message";
			throw new CiliaException(message, e);
		}
		
		return retval.toArray(new String[0]);
	}
}