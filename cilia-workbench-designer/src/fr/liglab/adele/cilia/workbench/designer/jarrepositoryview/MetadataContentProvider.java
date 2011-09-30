/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview;

import java.util.ArrayList;

import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Bundle;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.IPojo;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Port;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.Sender;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.GenericContentProvider;

/**
 * MetadataContentProvider.
 */
public class MetadataContentProvider extends GenericContentProvider {

	
	/**
	 * Initialize maps from model.
	 */
	public MetadataContentProvider(Bundle[] model) {
		
		parent.put(model, new Object[0]);
		children.put(model, new ArrayList<Object>());
		
		for (Bundle bundle : model) {
			parent.put(bundle, model);
			children.get(model).add(bundle);
			children.put(bundle, new ArrayList<Object>());
			IPojo ipojo = bundle.getMetadata();
			
			for (MediatorComponent mc : ipojo.getMediatorComponents()) {
				parent.put(mc, bundle);
				children.get(bundle).add(mc);
				children.put(mc, new ArrayList<Object>());
				
				for (Port p : mc.getPorts()) {
					parent.put(p, mc);
					children.get(mc).add(p);
					children.put(p, new ArrayList<Object>());
				}
			}
			
			for (Processor p : ipojo.getProcessors()) {
				parent.put(p, bundle);
				children.get(bundle).add(p);
				children.put(p, new ArrayList<Object>());
			}
			
			for (Scheduler s : ipojo.getSchedulers()) {
				parent.put(s, bundle);
				children.get(bundle).add(s);
				children.put(s, new ArrayList<Object>());
			}
			
			for (Dispatcher d : ipojo.getDispatchers()) {
				parent.put(d, bundle);
				children.get(bundle).add(d);
				children.put(d, new ArrayList<Object>());
			}
			
			for (Collector c : ipojo.getCollectors()) {
				parent.put(c, bundle);
				children.get(bundle).add(c);
				children.put(c, new ArrayList<Object>());
			}
			
			for (Sender s : ipojo.getSenders()) {
				parent.put(s, bundle);
				children.get(bundle).add(s);
				children.put(s, new ArrayList<Object>());
			}
			
			for (Adapter a : ipojo.getAdapters()) {
				parent.put(a, bundle);
				children.get(bundle).add(a);
				children.put(a, new ArrayList<Object>());
			}
		}
	}
}
