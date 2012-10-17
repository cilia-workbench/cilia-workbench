package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

public class PlatformChain implements ErrorsAndWarningsFinder, DisplayedInPropertiesView, Identifiable {

	private final String name;

	public PlatformChain(String name) {
		this.name = name;
	}

	@Override
	public Object getId() {
		return name;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		return CiliaFlag.generateTab(e1);
	}
}
