package fr.liglab.adele.cilia.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;

public class ListDialogMain {

	public static void main(String[] args) {
		
		List<String> items = new ArrayList<String>();
		items.add("one");
		items.add("two");
		items.add("three");
				
		ListDialog ld = new ListDialog(null, items);
		
		if (ld.open() == Window.OK) {
			System.out.println("OK pressed");
			for (String item : ld.getResult()) {
				System.out.println(item);
			}	
		}
		else {
			System.out.println("CANCEL pressed");
		}
	}
}
