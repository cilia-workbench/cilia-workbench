package fr.liglab.adele.cilia.workbench.restmonitoring.sandbox;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.HTTPhelper;

public class Test {

	
	public static void main(String[] args) {		
		HTTPhelper http = new HTTPhelper("localhost", 8080);
		
		String[] targets = { "/cilia", "/cilia/toto"};
		for (int i = 0; i < targets.length; i++) {
			try {
				http.get(targets[i]);
			} catch (CiliaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
