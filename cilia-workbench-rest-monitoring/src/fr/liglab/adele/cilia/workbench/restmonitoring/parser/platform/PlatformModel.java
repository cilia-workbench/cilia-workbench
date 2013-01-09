/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.parser.AbstractModel;
import fr.liglab.adele.cilia.workbench.common.parser.PhysicalResource;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Changeset.Operation;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformModel extends AbstractModel implements Mergeable, ErrorsAndWarningsFinder {

	public static final String ROOT_NODE_NAME = "cilia-platform";

	private PlatformID platformID;

	private List<PlatformChain> chains = new ArrayList<PlatformChain>();

	private final PlatformFile platformFile;

	public PlatformModel(PhysicalResource file, PlatformFile platformFile) throws CiliaException {
		super(file, ROOT_NODE_NAME);

		this.platformFile = platformFile;

		Document document = XMLHelpers.getDocument(file.getContentAsStream());
		Node root = getRootNode(document);
		platformID = new PlatformID(root);
	}

	public PlatformID getPlatformID() {
		return platformID;
	}

	public PlatformFile getPlatformFile() {
		return platformFile;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		ArrayList<Changeset> retval = new ArrayList<Changeset>();
		PlatformModel newInstance = (PlatformModel) other;

		// ON UPDATE : be careful with next line...
		retval.addAll(MergeUtil.computeUpdateChangeset(newInstance.getPlatformID(), platformID, "host", "port"));
		// if host or port change, we need to remove all chains (because
		// target platform changed...)
		if (retval.size() != 0) {
			for (PlatformChain chain : chains)
				retval.add(new Changeset(Operation.REMOVE, chain));
			chains.clear();
		}

		return retval;
	}

	public List<Changeset> mergeChains(String[] chainsList) {

		List<Changeset> retval = new ArrayList<Changeset>();

		// ADD
		for (String chain : chainsList) {
			if (getChain(chain) == null) {
				PlatformChain pc = new PlatformChain(chain, this);
				chains.add(pc);
				retval.add(new Changeset(Operation.ADD, pc));
			}
		}

		// REMOVE
		List<String> newList = new ArrayList<String>();
		for (String name : chainsList)
			newList.add(name);
		for (PlatformChain pc : chains) {
			String name = pc.getName();
			if (!newList.contains(name)) {
				retval.add(new Changeset(Operation.REMOVE, pc));
			}
		}
		for (Changeset c : retval)
			if (c.getOperation().equals(Operation.REMOVE))
				chains.remove(c.getObject());

		return retval;
	}

	public List<PlatformChain> getChains() {
		return chains;
	}

	public PlatformChain getChain(String chain) {
		for (PlatformChain pc : chains)
			if (pc.getName().equals(chain))
				return pc;

		return null;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = null;

		List<CiliaFlag> list = IdentifiableUtils.getErrorsNonUniqueId(this, chains);

		if (platformID.isValid() != null)
			e1 = new CiliaError(platformID.isValid(), this);

		return CiliaFlag.generateTab(list, e1);
	}
}
