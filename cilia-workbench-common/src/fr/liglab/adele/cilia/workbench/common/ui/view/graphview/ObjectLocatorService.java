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

import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;

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

	private Point getLocationByMemory(Object object) {
		return location.get(object);
	}

	// TODO For demo purpose... to be removed later
	private Point getLocationByName(Object object) {
		String name = null;

		// Find name
		// =========

		if (object instanceof ComponentRef) {
			ComponentRef component = (ComponentRef) object;
			name = component.getId();
		}

		if (name == null)
			return null;

		// Lookup by name
		// ==============

		if (name.equals("hl7-rest-adapter"))
			return new Point(22, 15);
		if (name.equals("hl7-filter") || name.equals("hapifilter"))
			return new Point(188, 15);
		if (name.equals("continua-filter"))
			return new Point(306, 15);
		if (name.equals("phr-translation") || name.equals("translation"))
			return new Point(476, 15);
		if (name.equals("phr-integration") || name.equals("integration"))
			return new Point(687, 15);

		if (name.equals("error-manager"))
			return new Point(377, 110);
		if (name.equals("acknowledgement"))
			return new Point(369, 173);

		return null;
	}

	public Point getLocation(Object object) {

		Point locByMem = getLocationByMemory(object);
		if (locByMem != null)
			return locByMem;

		Point locByName = getLocationByName(object);
		if (locByName != null)
			return locByName;

		return null;
	}

}
