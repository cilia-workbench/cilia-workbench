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
package fr.liglab.adele.cilia.workbench.common.ui.view.graphview;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.liglab.adele.cilia.workbench.common.Activator;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.ui.preferencePage.CiliaRootPreferencePage;

/**
 * 
 * @author Etienne Gandrille
 */
public class ObjectLocatorService {

	private static ObjectLocatorService INSTANCE = new ObjectLocatorService();
	private Map<String, Point> location = null;

	private ObjectLocatorService() {
		Activator.getInstance().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(CiliaRootPreferencePage.PREFERENCES_FILE_PATH)) {
					location = loadPreferencesContent(CiliaRootPreferencePage.PREFERENCES_FILE_PATH);
				}
			}
		});
		location = loadPreferencesContent(CiliaRootPreferencePage.PREFERENCES_FILE_PATH);
	}

	public static ObjectLocatorService getInstance() {
		return INSTANCE;
	}

	public void updateLocation(ComponentRef component, Point point) {
		String key = component.getKey();
		location.put(key, point);
		writePreferencesContent(CiliaRootPreferencePage.PREFERENCES_FILE_PATH, location);
	}

	public Point getLocation(ComponentRef component) {
		String key = component.getKey();
		return location.get(key);
	}

	// =======================
	// FILE SYSTEM INTERACTION
	// =======================

	private static File getRepositoryLocation(String preferencesStoreName) {
		IPreferenceStore store = Activator.getInstance().getPreferenceStore();
		File file = new File(store.getString(preferencesStoreName));
		if (!file.isFile())
			return null;
		return file;
	}

	private static Map<String, Point> loadPreferencesContent(String preferencesStoreName) {
		Map<String, Point> retval = new HashMap<String, Point>();
		Scanner scanner = null;
		try {
			File file = getRepositoryLocation(preferencesStoreName);
			if (file != null) {
				scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] fields = line.split(" ", 3);
					if (fields.length == 3) {
						try {
							int x = Integer.parseInt(fields[0]);
							int y = Integer.parseInt(fields[1]);
							String key = fields[2];
							if (!Strings.isNullOrEmpty(key)) {
								retval.put(key, new Point(x, y));
							}
						} catch (Exception e) {
							// parse error : just ignore this line
						}
					}
				}
			}
		} catch (Exception e) {
			// scanner error
			e.printStackTrace();
		}

		if (scanner != null) {
			scanner.close();
		}

		return retval;
	}

	private static boolean writePreferencesContent(String preferencesStoreName, Map<String, Point> content) {

		File file = getRepositoryLocation(preferencesStoreName);
		if (file != null) {

			StringBuilder sb = new StringBuilder();
			for (String key : content.keySet()) {
				Point point = content.get(key);
				sb.append(point.x);
				sb.append(" ");
				sb.append(point.y);
				sb.append(" ");
				sb.append(key);
				sb.append("\n");
			}
			String text = sb.toString();
			return writeFile(text, file);
		}
		return false;
	}

	private static boolean writeFile(String fileContent, File file) {
		boolean retval = true;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			bufferedWriter.write(fileContent);
			bufferedWriter.flush();
		} catch (IOException ioe) {
			retval = false;
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retval;
	}
}
