/**
 * Copyright 2012-2013 France Télécom 
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
package fr.liglab.adele.cilia.workbench.common.ui.dialog;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * 
 * @author Etienne Gandrille
 */
public class IInputValidatorBuilder {

	public static IInputValidator getNonNullOrEmptyValidator() {
		return new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText == null)
					return "text can't be null";
				if (newText.trim().length() == 0)
					return "text cant't be empty";
				return null;
			}
		};
	}

	public static IInputValidator getNonNullEmptySpaceValidator() {
		return new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText == null)
					return "text can't be null";
				if (newText.trim().length() == 0)
					return "text cant't be empty";
				if (newText.indexOf(' ') != -1)
					return "text can't contain spaces";
				return null;
			}
		};
	}
}
