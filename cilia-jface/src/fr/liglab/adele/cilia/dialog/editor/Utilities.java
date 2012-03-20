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
package fr.liglab.adele.cilia.dialog.editor;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Etienne Gandrille
 */
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