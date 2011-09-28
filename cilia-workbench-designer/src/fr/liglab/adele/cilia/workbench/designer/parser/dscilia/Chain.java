package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.designer.parser.XMLutil;
import fr.liglab.adele.cilia.workbench.designer.parser.metadata.MetadataException;

public class Chain {

	private String id;
	private List<AdapterInstance> adapters = new ArrayList<AdapterInstance>();
	private List<MediatorInstance> mediators = new ArrayList<MediatorInstance>();
	private List<Binding> bindings = new ArrayList<Binding>();
	
	public Chain(Node node) throws MetadataException {
		XMLutil.setRequiredAttribute(node, "id", this, "id");
		
		Node rootAdapters = XMLutil.findChild(node, "adapters");
		if (rootAdapters != null) {
			Node[] ais = XMLutil.findChildren(rootAdapters, "adapter-instance");
			for (Node ai : ais)
				adapters.add(new AdapterInstance(ai));
		}
		
		Node rootMediators = XMLutil.findChild(node, "mediators");
		if (rootMediators != null) {
			Node[] mis = XMLutil.findChildren(rootMediators, "mediator-instance");
			for (Node mi : mis)
				mediators.add(new MediatorInstance(mi));
		}
		
		Node rootBindings = XMLutil.findChild(node, "bindings");
		if (rootBindings != null) {
			Node[] bis = XMLutil.findChildren(rootBindings, "binding");
			for (Node bi : bis)
				bindings.add(new Binding(bi));
		}
	}
	
	@Override
	public String toString() {
		return id;
	}
}
