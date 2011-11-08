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
