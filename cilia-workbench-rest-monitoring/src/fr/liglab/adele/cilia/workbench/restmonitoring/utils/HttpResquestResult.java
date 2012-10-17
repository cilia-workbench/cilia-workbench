package fr.liglab.adele.cilia.workbench.restmonitoring.utils;

import org.apache.http.StatusLine;

public class HttpResquestResult {

	private final StatusLine status;
	private final String message;
	
	public HttpResquestResult(StatusLine status, String message) {
		this.status = status;
		this.message = message;
	}

	public StatusLine getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
}
