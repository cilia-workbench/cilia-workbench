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
package fr.liglab.adele.cilia.workbench.designer.jarrepositoryview;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Adapter;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarFile;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.CiliaJarModel;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Collector;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Port;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Processor;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.parser.ciliajar.Sender;
import fr.liglab.adele.cilia.workbench.designer.repositoryview.GenericContentProvider;

/**
 * MetadataContentProvider.
 */
public class JarContentProvider extends GenericContentProvider {

	
	/**
	 * Initialize maps from model.
	 */
	public JarContentProvider(List<CiliaJarFile> model) {
		
		addRoot(model);
		
		for (CiliaJarFile bundle : model) {
			
			addChild(model, bundle);
			CiliaJarModel ipojo = bundle.getModel();
			
			for (MediatorComponent mc : ipojo.getMediatorComponents()) {
				addChild(bundle, mc);
				for (Port p : mc.getPorts()) {
					addChild(mc, p);
				}
			}
			
			for (Processor p : ipojo.getProcessors()) {
				addChild(bundle, p);
			}
			
			for (Scheduler s : ipojo.getSchedulers()) {
				addChild(bundle, s);
			}
			
			for (Dispatcher d : ipojo.getDispatchers()) {
				addChild(bundle, d);
			}
			
			for (Collector c : ipojo.getCollectors()) {
				addChild(bundle, c);
			}
			
			for (Sender s : ipojo.getSenders()) {
				addChild(bundle, s);
			}
			
			for (Adapter a : ipojo.getAdapters()) {
				addChild(bundle, a);
			}
		}
	}
}
