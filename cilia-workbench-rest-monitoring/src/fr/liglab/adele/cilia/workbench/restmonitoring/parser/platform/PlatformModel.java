package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.io.File;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.misc.ReflectionUtil;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

public class PlatformModel implements DisplayedInPropertiesView, Mergeable {
		
	public static final String XML_NODE_NAME = "cilia-platform";

	private String host;
	private String port;
	private File file;

	public PlatformModel(File file) throws CiliaException {
		this.file = file;

		Document document = XMLHelpers.getDocument(file);
		Node root = getRootNode(document);

		ReflectionUtil.setAttribute(root, "host", this, "host");
		ReflectionUtil.setAttribute(root, "port", this, "port");		
	}

	private static Node getRootNode(Document document) throws CiliaException {
		return XMLHelpers.getRootNode(document, XML_NODE_NAME);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		//TODO NYI
		return null;
	}
}
