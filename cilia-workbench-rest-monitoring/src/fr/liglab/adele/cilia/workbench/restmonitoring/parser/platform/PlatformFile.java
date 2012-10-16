package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.service.AbstractFile;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
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
		// TODO NYI
		return new ArrayList<Changeset>();
	}

	@Override
	public Object getObjectForComputingProperties() {
		if (model == null)
			return this;
		else
			return model;
	}
}
