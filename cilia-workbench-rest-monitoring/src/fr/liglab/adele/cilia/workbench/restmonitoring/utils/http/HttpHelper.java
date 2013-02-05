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
package fr.liglab.adele.cilia.workbench.restmonitoring.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;

/**
 * 
 * @author Etienne Gandrille
 */
public class HttpHelper {

	private static final int timeout = 3000;

	private HttpHelper() {
		// don't instantiate
	}

	public static String get(PlatformID platformID, String target) throws CiliaException {

		String url = getURL(platformID, target);
		HttpUriRequest httpRequest = new HttpGet(url);
		HttpClient httpClient = getClient();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		try {
			String retval = httpClient.execute(httpRequest, responseHandler);
			httpClient.getConnectionManager().shutdown();
			return retval;
		} catch (ConnectTimeoutException e) {
			httpClient.getConnectionManager().shutdown();
			throw new CiliaException("Can't joint " + url + " before timeout (" + timeout + "ms)");
		} catch (ClientProtocolException e) {
			httpClient.getConnectionManager().shutdown();
			throw new CiliaException("Can't joint " + url, e);
		} catch (IOException e) {
			httpClient.getConnectionManager().shutdown();
			throw new CiliaException("Can't joint " + url, e);
		}
	}

	private static HttpClient getClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		return new DefaultHttpClient(params);
	}

	private static String getURL(PlatformID platformID, String target) {
		return "http://" + platformID.getHost() + ":" + platformID.getPort() + target;
	}

	public static String put(PlatformID platformID, String target, String data) throws CiliaException {
		String url = getURL(platformID, target);
		HttpPut httpRequest = new HttpPut(url);

		// entity management
		try {
			httpRequest.setEntity(new StringEntity(data));
		} catch (UnsupportedEncodingException e) {
			throw new CiliaException("can't add entity to HTTP PUT request", e);
		}

		// HTTP request
		HttpClient httpClient = getClient();
		try {
			String retval = httpClient.execute(httpRequest, new BasicResponseHandler());
			httpClient.getConnectionManager().shutdown();
			return retval;
		} catch (Exception e) {
			httpClient.getConnectionManager().shutdown();
			throw new CiliaException("can't perform HTTP PUT request", e);
		}
	}
}
