package fr.liglab.adele.cilia.workbench.designer.service.specreposervice;

import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.parser.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Port;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.Property;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecFile;
import fr.liglab.adele.cilia.workbench.designer.parser.spec.SpecModel;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;

/**
 * Content provider used by the spec repository.
 */
public class SpecContentProvider extends GenericContentProvider {

	/**
	 * Initialize maps from model.
	 */
	public SpecContentProvider(List<SpecFile> root) {

		addRoot(root);

		for (SpecFile file : root) {

			addRelationship(root, file);

			SpecModel model = file.getModel();
			if (model != null) {
				for (MediatorSpec spec : model.getMediatorSpecs()) {
					addRelationship(file, spec);

					addRelationship(spec, spec.getScheduler());
					addRelationship(spec, spec.getProcessor());
					addRelationship(spec, spec.getDispatcher());

					for (Property property : spec.getProperties())
						addRelationship(spec, property);

					for (Port port : spec.getPorts())
						addRelationship(spec, port);
				}
			}
		}
	}
}
