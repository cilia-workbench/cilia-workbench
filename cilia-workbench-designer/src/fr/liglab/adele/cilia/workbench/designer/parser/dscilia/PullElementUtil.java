package fr.liglab.adele.cilia.workbench.designer.parser.dscilia;

import java.util.Iterator;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.RepoElement;

public class PullElementUtil {
	
	public static RepoElement pullRepoElement(List<RepoElement> newInstance, String id) {
		for (Iterator<RepoElement> itr = newInstance.iterator(); itr.hasNext();) {
			RepoElement element = itr.next();
			if (element.getFilePath().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
	
	public static Binding pullBinding(Chain newInstance, String from, String to) {
		for (Iterator<Binding> itr = newInstance.getBindings().iterator(); itr.hasNext();) {
			Binding element = itr.next();
			if (element.getSourceId().equals(from) && element.getDestinationId().equals(to)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static MediatorInstance pullMediatorInstance(Chain newInstance, String id) {
		for (Iterator<MediatorInstance> itr = newInstance.getMediators().iterator(); itr.hasNext();) {
			MediatorInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static AdapterInstance pullAdapterInstance(Chain newInstance, String id) {
		for (Iterator<AdapterInstance> itr = newInstance.getAdapters().iterator(); itr.hasNext();) {
			AdapterInstance element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
	
	public static Chain pullChain(Dscilia newInstance, String id) {
		for (Iterator<Chain> itr = newInstance.getChains().iterator(); itr.hasNext();) {
			Chain element = itr.next();
			if (element.getId().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
