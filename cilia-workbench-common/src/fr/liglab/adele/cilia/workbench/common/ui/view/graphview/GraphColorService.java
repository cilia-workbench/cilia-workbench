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

/**
 * Service for getting colors to paint graphs. This service has been designed to
 * be used throw a settings dialog in the future.
 * 
 * @author Etienne Gandrille
 */
public class GraphColorService {

	private static GraphColorService INSTANCE = null;

	private final GraphConfig config;

	private GraphColorService() {
		config = GraphConfig.DEFAULT;
	}

	public static GraphColorService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new GraphColorService();
		return INSTANCE;
	}

	public GraphConfig getConfig() {
		return config;
	}
}
