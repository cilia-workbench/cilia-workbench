package fr.liglab.adele.cilia.dialog.specdialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;

public class SpecDialogMain {
	public static void main(String[] args) {
		
		List<String> items = new ArrayList<String>();
		items.add("one");
		items.add("two");
		items.add("three");
				
		SpecDialog sd = new SpecDialog(null);
		
		if (sd.open() == Window.OK) {
			System.out.println("OK pressed");
		}
		else {
			System.out.println("CANCEL pressed");
		}
	}
}
