package fr.liglab.adele.cilia.workbench.designer.chaindesignerview;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.AdapterInstance;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.MediatorInstance;

public class ChainGraphContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	private Chain model = null;

	public ChainGraphContentProvider() {
	}

	@Override
	public Object[] getConnectedTo(Object entity) {

		if (model == null)
			return new Object[0];
		
		if (entity instanceof AdapterInstance) {
			AdapterInstance ai = (AdapterInstance) entity;
			return model.getDestinations(ai);

		} else if (entity instanceof MediatorInstance) {
			MediatorInstance mi = (MediatorInstance) entity;
			return model.getDestinations(mi);
		}

		return new Object[0];
	}

	public void setModel(Chain model) {
		this.model = model;
	}
}
