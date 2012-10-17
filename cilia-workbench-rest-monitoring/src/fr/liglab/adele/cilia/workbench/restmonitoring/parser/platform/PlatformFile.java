package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.service.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesViewWithForward;

public class PlatformFile extends AbstractFile<PlatformModel> implements Mergeable, DisplayedInPropertiesViewWithForward {

	public PlatformFile(File file) {
		super(file);

		try {
			model = new PlatformModel(file);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		
		PlatformFile newInstance = (PlatformFile) other;

		PlatformModel oldModel = getModel();
		List<Changeset> result = MergeUtil.mergeObjectsFields(newInstance, this, "model");
		PlatformModel newModel = getModel();

		// Because the Model file is not displayed in the view, here is a little
		// piece of code for handling this very special case...
		for (Changeset c : result) {

			// XML file becomes valid
			if (c.getOperation().equals(Operation.ADD) && c.getObject() == newModel) {
				// TODO ajouter tout le NOUVEAU contenu car le XML devient valide
			}

			// XML file becomes invalid
			else if (c.getOperation().equals(Operation.REMOVE) && c.getObject() == oldModel) {
				// TODO supprimer tout l'ANCIEN contenu car le XML n'est plus valide
			}

			// Other event, deeper in hierarchy
			else
				retval.add(c);
		}

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}
		
	@Override
	public Object getObjectForComputingProperties() {
		if (model == null)
			return this;
		else
			return model;
	}
}
