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
package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fr.liglab.adele.cilia.workbench.designer.metadataparser.Adapter;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Bundle;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Collector;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Dispatcher;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.IPojo;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Processor;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Scheduler;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.Sender;

/**
 * MetadataContentProvider.
 */
public class MetadataContentProvider implements ITreeContentProvider {

	/** Maps to get the children from the parent. */
	Map<Object, List<Object>> children = new HashMap<Object, List<Object>>();
	
	/** Map to get the parent from a child. */
	Map<Object, Object> parent = new HashMap<Object, Object>();
	
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

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return parent.get(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> a = children.get(parentElement);
		if (a == null)
			return new Object[0];
		return a.toArray();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return (getChildren(element).length != 0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
