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

public class MetadataContentProvider implements ITreeContentProvider {

	Map<Object, List<Object>> children = new HashMap<Object, List<Object>>();
	Map<Object, Object> parent = new HashMap<Object, Object>();
	
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

	@Override
	public Object getParent(Object element) {
		return parent.get(element);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> a = children.get(parentElement);
		if (a == null)
			return new Object[0];
		return a.toArray();
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return (getChildren(element).length != 0);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
