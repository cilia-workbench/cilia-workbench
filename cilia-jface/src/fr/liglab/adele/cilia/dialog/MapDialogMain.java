package fr.liglab.adele.cilia.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.window.Window;

public class MapDialogMain {

	public static void main(String[] args) {
		
		Map<String, String> input = new HashMap<String, String>();
		input.put("width", "100");
		input.put("border", "2px");
		input.put("scheduler", "imediate");

		MapDialog md = new MapDialog(null, input);
				
		if (md.open() == Window.OK) {
			System.out.println("OK pressed");
			for (String key : md.getResult().keySet()) {
				System.out.println(key + " : " + md.getResult().get(key));
			}	
		}
		else {
			System.out.println("CANCEL pressed");
		}
	}
}
