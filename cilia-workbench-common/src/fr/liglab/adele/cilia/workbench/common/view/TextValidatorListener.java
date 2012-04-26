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
package fr.liglab.adele.cilia.workbench.common.view;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A simple text validator listener, for validating SWT text boxes. The
 * validations constraints are defined in the {@link #IsValidString(String)}
 * method.
 * 
 * @author Etienne Gandrille
 */
public class TextValidatorListener implements Listener {

	/** shared instance */
	private static TextValidatorListener INSTANCE = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (!IsValidString(event.text))
			event.doit = false;
	}

	private TextValidatorListener() {
	}

	/**
	 * Gets the text validator listner.
	 * 
	 * @return the text validator listner
	 */
	public static Listener getTextValidatorListner() {
		if (INSTANCE == null)
			INSTANCE = new TextValidatorListener();
		return INSTANCE;
	}

	/**
	 * The validation logic.
	 * 
	 * @param str
	 *            the string to be tested
	 * @return true if str is valid, false otherwise.
	 */
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