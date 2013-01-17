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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;

/**
 * 
 * @author Etienne Gandrille
 */
public class ObjectLocatorService {

	private static ObjectLocatorService INSTANCE = new ObjectLocatorService();
	Map<Object, Point> location = new HashMap<Object, Point>();

	private ObjectLocatorService() {
	}

	public static ObjectLocatorService getInstance() {
		return INSTANCE;
	}

	public void updateLocation(Object object, Point point) {
		location.put(object, point);
	}

	public Point getLocation(Object object) {
		return location.get(object);
	}
}
