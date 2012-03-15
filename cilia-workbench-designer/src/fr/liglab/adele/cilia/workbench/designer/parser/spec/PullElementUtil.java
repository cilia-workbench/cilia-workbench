package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.Iterator;
import java.util.List;

public class PullElementUtil {

	public static SpecFile pullRepoElement(List<SpecFile> elements, String id) {
		for (Iterator<SpecFile> itr = elements.iterator(); itr.hasNext();) {
			SpecFile element = itr.next();
			if (element.getFilePath().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
	
	public static MediatorSpec pullMediatorSpec(SpecModel elements, String id, String namespace) {
		for (Iterator<MediatorSpec> itr = elements.getMediatorSpecs().iterator(); itr.hasNext();) {
			MediatorSpec element = itr.next();
			if (element.getId().equals(id) && element.getNamespace().equals(namespace)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Port pullPort(MediatorSpec mediator, String name, String type) {
		for (Iterator<Port> itr = mediator.getPorts().iterator(); itr.hasNext();) {
			Port element = itr.next();
			if (element.getName().equals(name) && element.getType().equals(type)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Property pullProperty(MediatorSpec mediator, String key) {
		for (Iterator<Property> itr = mediator.getProperties().iterator(); itr.hasNext();) {
			Property element = itr.next();
			if (element.getKey().equals(key)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Parameter pullParameter(ComponentPart componentPart, String name) {
		for (Iterator<Parameter> itr = componentPart.getParameters().iterator(); itr.hasNext();) {
			Parameter element = itr.next();
			if (element.getName().equals(name)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
