package fr.liglab.adele.cilia.workbench.restmonitoring.service.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.restmonitoring.misc.preferencepage.RestMonitoringPreferencePage;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformFile;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformModel;

public class PlatformRepoService  extends AbstractRepoService<PlatformFile, PlatformModel> implements ErrorsAndWarningsFinder {

	
	/** Singleton instance */
	private static PlatformRepoService INSTANCE;

	/** The key used to search the repository path into the preferences store. */
	private static String PREFERENCE_PATH_KEY = RestMonitoringPreferencePage.REST_PLATFORM_REPOSITORY_PATH;

	/** Spec files extension. */
	public final static String ext = ".pf";

	/** Repository Name */
	private final static String repositoryName = "Cilia Platforms";

	public static PlatformRepoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlatformRepoService();
			INSTANCE.updateModel();
		}
		return INSTANCE;
	}

	private PlatformRepoService() {
		super(PREFERENCE_PATH_KEY, ext, repositoryName);
	}

	public void updateModel() {
		File[] list = getFiles();
		List<PlatformFile> elements = new ArrayList<PlatformFile>();
		for (File file : list) {
			elements.add(new PlatformFile(file));
		}

		// Updates existing model with computed model
		List<Changeset> changes = null;
		try {
			changes = merge(elements);
		} catch (CiliaException e) {
			e.printStackTrace();
		}

		// Update content provider
		contentProvider = new PlatformContentProvider(model);

		// Update markers relative to this repository
		updateMarkers();

		// Sends notifications
		notifyListeners(changes);
	}

	/**
	 * Merge a list of repo element into the current model. Only differences
	 * between the argument and the model are merge back into the model.
	 * 
	 * @param repoElements
	 *            a new model
	 * @return a list of changesets, which can be empty.
	 */
	private List<Changeset> merge(List<PlatformFile> repoElements) throws CiliaException {

		ArrayList<Changeset> retval = new ArrayList<Changeset>();

		retval.addAll(MergeUtil.mergeLists(repoElements, this.model));

		// path update
		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	protected String getContentForNewFile() {

		// TODO erreur : la balise racine contient host et port... 
		
		return "<" + PlatformModel.XML_NODE_NAME + ">\n</" + PlatformModel.XML_NODE_NAME + ">";
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> errorList = new ArrayList<CiliaFlag>();

		// TODO
		
		return CiliaFlag.generateTab(errorList);
	}
}
