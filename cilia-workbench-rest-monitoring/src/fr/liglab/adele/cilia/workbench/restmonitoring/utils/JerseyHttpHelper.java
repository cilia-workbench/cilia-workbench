package fr.liglab.adele.cilia.workbench.restmonitoring.utils;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.apache.http.client.utils.URIBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;

public class JerseyHttpHelper {

	public static String get(PlatformID platformID, String suburl) {

		Client client = Client.create();

		String url = "http://" + platformID.getHost() + ":" + platformID.getPort() + suburl;
		url = "http://10.194.3.114:8080/cilia/";
		url = "http://10.194.3.114:8080/restAdapter/rest/continua";

		URI uri;
		try {
			uri = new URI(url);

			String host = uri.getHost();
			String path = uri.getPath();
			int port = uri.getPort();

			WebResource resource = client.resource(uri);
			String o = resource.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
