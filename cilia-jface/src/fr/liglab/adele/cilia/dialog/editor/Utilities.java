package fr.liglab.adele.cilia.dialog.editor;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class Utilities {

	private static Listener textValidatorListner = null;
	
	public static Listener getTextValidatorListner() {
		if (textValidatorListner == null)
			textValidatorListner = getTextValidatorListener();
		
		return textValidatorListner;
	}
	
	private static Listener getTextValidatorListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (!IsValidString(event.text))
					event.doit = false;
			}
		};
	}
	
	private static boolean IsValidString(String str) {
		char[] chars = new char[str.length()];
		str.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			boolean nb = (chars[i] >= '0' && chars[i] <= '9');
			boolean min = (chars[i] >= 'a' && chars[i] <= 'z');
			boolean maj = (chars[i] >= 'A' && chars[i] <= 'Z');
			boolean spec = (chars[i] == '-' || chars[i] == '_' || chars[i] == '.');
			
			boolean valid = (nb || min || maj || spec);
			
			if (!valid) {
				return false;
			}
		}
		
		return true;
	}
}