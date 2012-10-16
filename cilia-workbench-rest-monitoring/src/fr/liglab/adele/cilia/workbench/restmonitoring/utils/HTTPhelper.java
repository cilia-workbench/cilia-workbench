package fr.liglab.adele.cilia.workbench.restmonitoring.utils;

import java.io.IOException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.protocol.HttpRequestExecutor;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

public class HTTPhelper {

	HttpHost host;

	HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
	DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
	ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
	
	public HTTPhelper(String host, int port) {
		this.host = new HttpHost(host, port);
	}
	
	public void get(String target) throws CiliaException {
		if (conn != null)
			HttpSimpleAPI.getInstance().httpRequest(conn, host, httpexecutor, connStrategy, "GET", target);
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
