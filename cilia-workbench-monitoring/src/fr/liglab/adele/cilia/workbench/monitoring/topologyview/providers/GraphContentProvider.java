package fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.BindingReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview.NodeElement;

public class GraphContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	@Override
	public Object[] getConnectedTo(Object entity) {

		if (entity instanceof NodeElement) {
			BindingReadOnly bindings[];
			MediatorReadOnly element = ((NodeElement) entity).getElement();
			bindings = element.getOutBindings();
			
			List<Object> retval = new ArrayList<Object>();
			if (bindings != null) {
				for (BindingReadOnly binding : bindings) {
					retval.add(new NodeElement(binding.getTargetMediator()));
				}
			}
			return retval.toArray();
		}

		throw new RuntimeException("Type not supported");
	}
}
