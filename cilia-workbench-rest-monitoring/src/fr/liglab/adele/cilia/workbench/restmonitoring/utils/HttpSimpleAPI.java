package fr.liglab.adele.cilia.workbench.restmonitoring.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

public class HttpSimpleAPI {

	private HttpParams params = initParameters();
	
	private HttpProcessor httpproc = initProcessor();

	private static HttpSimpleAPI INSTANCE = null;
	
	private HttpSimpleAPI() {
	}
	
	public static HttpSimpleAPI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HttpSimpleAPI();
		return INSTANCE;
	}
	
	
	private static HttpParams initParameters() {
		
		HttpParams params = new SyncBasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		return params;
	}
	
	private static HttpProcessor initProcessor() {
		HttpProcessor httpproc = new ImmutableHttpProcessor(
				new HttpRequestInterceptor[] {
						// Required protocol interceptors
						new RequestContent(), new RequestTargetHost(),
						// Recommended protocol interceptors
						new RequestConnControl(), new RequestUserAgent(),
						new RequestExpectContinue() });
		
		return httpproc;
	}
	
	
	public HttpResquestResult httpRequest(DefaultHttpClientConnection conn, HttpHost host, HttpRequestExecutor httpexecutor, ConnectionReuseStrategy connStrategy, String operation, String target) throws CiliaException {
		
		// socket
		if (!conn.isOpen()) {
			Socket socket;
			try {
				socket = new Socket(host.getHostName(), host.getPort());
			} catch (UnknownHostException e) {
				throw new CiliaException("Socket binding error : unknown host " + host.getHostName(), e);
			} catch (IOException e) {
				throw new CiliaException("I/O error while creating socket", e);
			}
			try {
				conn.bind(socket, params);
			} catch (IOException e) {
				throw new CiliaException("I/O error while binding socket", e);
			}
		}
		
		// building request
		BasicHttpRequest request = new BasicHttpRequest(operation, target);
		request.setParams(params);
		
		// context
		HttpContext context = new BasicHttpContext(null);
		context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
		context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
		
		// sending request		
		try {
			httpexecutor.preProcess(request, httpproc, context);
		} catch (HttpException e) {
			throw new CiliaException("HTTP exception during httpExecutor preprocess", e);
		} catch (IOException e) {
			throw new CiliaException("I/O exception during httpExecutor preprocess", e);
		}
		HttpResponse response;
		try {
			response = httpexecutor.execute(request, conn,	context);
		} catch (IOException e) {
			throw new CiliaException("I/O exception during httpExecutor execute", e);
		} catch (HttpException e) {
			throw new CiliaException("HTTP exception during httpExecutor execute", e);
		}
		response.setParams(params);
		try {
			httpexecutor.postProcess(response, httpproc, context);
		} catch (HttpException e) {
			throw new CiliaException("HTTP exception during response parameters preparation", e);
		} catch (IOException e) {
			throw new CiliaException("I/O exception during response parameters preparation", e);
		}

		
		StatusLine status = response.getStatusLine();
		String message;
		try {
			message = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			throw new CiliaException("parse error while reading status line", e);
		} catch (IOException e) {
			throw new CiliaException("I/O exception while reading status line", e);
		}
		
		HttpResquestResult retval = new HttpResquestResult(status, message);
		
		if (!connStrategy.keepAlive(response, context))
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return retval;
	}
}
