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
package fr.liglab.adele.cilia.dialog.specdialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;


/**
 *
 * @author Etienne Gandrille
 */
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
