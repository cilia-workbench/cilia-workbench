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

import java.io.IOException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.protocol.HttpRequestExecutor;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

/**
 * 
 * @author Etienne Gandrille
 */
public class HttpHelper {

	HttpHost host;

	HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
	DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
	ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
	
	public HttpHelper(String host, int port) {
		this.host = new HttpHost(host, port);
	}
	
	public HttpResquestResult get(String target) throws CiliaException {
		if (conn != null)
			return HttpSimpleAPI.getInstance().httpRequest(conn, host, httpexecutor, connStrategy, "GET", target);
		else
			throw new CiliaException("Connexion is closed!");
	}
	
	
	public void close() throws CiliaException {
		try {
			conn.close();
			conn = null;
		} catch (IOException e) {
			conn = null;
			throw new CiliaException("Error while closing connexion!");
		}		
	}
}
