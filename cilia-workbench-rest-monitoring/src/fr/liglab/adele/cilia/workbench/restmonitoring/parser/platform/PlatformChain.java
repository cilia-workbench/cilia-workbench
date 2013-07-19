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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.identifiable.PlatformID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Chain;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;
import fr.liglab.adele.cilia.workbench.designer.service.chain.abstractcompositionsservice.AbstractCompositionsRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.utils.http.CiliaRestHelper;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChain extends Chain {

	private final PlatformModel platform;

	/* null if not linked to a reference architecture */
	private NameNamespaceID refArchitectureID = null;

	public PlatformChain(String name, PlatformModel platform, NameNamespaceID refArchitectureID) {
		super(name);
		this.platform = platform;
		this.refArchitectureID = refArchitectureID;
	}

	public PlatformChain(JSONObject json, PlatformModel platform) throws CiliaException {
		super(getJSONname(json));

		this.platform = platform;
		this.refArchitectureID = null;

		PlatformID platformId = platform.getPlatformID();
		String chainId = getName();

		try {
			JSONArray mediatorsList = json.getJSONArray("Mediators");
			for (int i = 0; i < mediatorsList.length(); i++) {
				String mediatorName = (String) mediatorsList.get(i);

				JSONObject jsonNode = CiliaRestHelper.getMediatorContent(platform.getPlatformID(), getName(), mediatorName);
				String state = jsonNode.getString("State");
				String type = jsonNode.getString("Type");
				String namespace = jsonNode.getString("Namespace");
				NameNamespaceID mediatorTypeID = new NameNamespaceID(type, namespace);

				mediators.add(new MediatorInstanceRef(mediatorName, mediatorTypeID, state, platformId, chainId));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing mediators list", e);
		}

		try {
			JSONObject adaptersRoot = json.getJSONObject("Adapters");

			if (adaptersRoot.has("in-only")) {
				JSONArray inAdaptersList = adaptersRoot.getJSONArray("in-only");
				for (int i = 0; i < inAdaptersList.length(); i++) {
					String adapterName = (String) inAdaptersList.get(i);
					NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);

					JSONObject jsonNode = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), getName(), adapterName);
					String state = jsonNode.getString("State");
					adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, state, platformId, chainId));
				}
			}

			if (adaptersRoot.has("out-only")) {
				JSONArray outAdaptersList = adaptersRoot.getJSONArray("out-only");
				for (int i = 0; i < outAdaptersList.length(); i++) {
					String adapterName = (String) outAdaptersList.get(i);
					NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);

					JSONObject jsonNode = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), getName(), adapterName);
					String state = jsonNode.getString("State");
					adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, state, platformId, chainId));
				}
			}

			if (adaptersRoot.has("in-out")) {
				JSONArray inOutAdaptersList = adaptersRoot.getJSONArray("in-out");
				for (int i = 0; i < inOutAdaptersList.length(); i++) {
					String adapterName = (String) inOutAdaptersList.get(i);
					NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);

					JSONObject jsonNode = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), getName(), adapterName);
					String state = jsonNode.getString("State");
					adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, state, platformId, chainId));
				}
			}

			if (adaptersRoot.has("unknown")) {
				JSONArray unknownAdaptersList = adaptersRoot.getJSONArray("unknown");
				for (int i = 0; i < unknownAdaptersList.length(); i++) {
					String adapterName = (String) unknownAdaptersList.get(i);
					NameNamespaceID adapterTypeID = getAdapterTypeID(platform, getName(), adapterName);

					JSONObject jsonNode = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), getName(), adapterName);
					String state = jsonNode.getString("State");
					adapters.add(new AdapterInstanceRef(adapterName, adapterTypeID, state, platformId, chainId));
				}
			}

		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}

		try {
			JSONArray bindingsList = json.getJSONArray("Bindings");
			for (int i = 0; i < bindingsList.length(); i++) {
				JSONObject binding = (JSONObject) bindingsList.get(i);
				bindings.add(new BindingInstance(binding, platformId, chainId));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}
	}

	@Override
	public String toString() {
		if (refArchitectureID == null)
			return Strings.nullToEmpty(getName());
		else
			return Strings.nullToEmpty(getName() + " --> " + refArchitectureID.toString());
	}

	public NameNamespaceID getRefArchitectureID() {
		return refArchitectureID;
	}

	public void setRefArchitectureID(NameNamespaceID refArchitectureID) {
		this.refArchitectureID = refArchitectureID;
	}

	public AbstractChain getRefArchitecture() {
		if (refArchitectureID == null)
			return null;
		return AbstractCompositionsRepoService.getInstance().findChain(refArchitectureID);
	}

	private static String getJSONname(JSONObject json) {
		try {
			return json.getString("ID");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static NameNamespaceID getAdapterTypeID(PlatformModel platform, String chainName, String adapterName) throws CiliaException {
		try {
			JSONObject adapter = CiliaRestHelper.getAdapterContent(platform.getPlatformID(), chainName, adapterName);
			String type = adapter.getString("Type");
			String namespace = adapter.getString("Namespace");
			return new NameNamespaceID(type, namespace);
		} catch (JSONException e) {
			throw new CiliaException(e);
		}
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);

		/* DON'T MERGE refArchitectureID ! */

		/*
		 * PlatformChain newRef = (PlatformChain) other;
		 * retval.addAll(MergeUtil.
		 * computeUpdateChangeset(newRef.getRefArchitectureID(),
		 * refArchitectureID, "name"));
		 * retval.addAll(MergeUtil.computeUpdateChangeset
		 * (newRef.getRefArchitectureID(), refArchitectureID, "namespace"));
		 */

		return retval;
	}

	@Override
	public String getId() {
		return getName();
	}

	public PlatformModel getPlatform() {
		return platform;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		CiliaError e1 = null;

		if (refArchitectureID != null && getRefArchitecture() == null)
			e1 = new CiliaError("Can't find reference architecture " + refArchitectureID.toString(), this);

		return CiliaFlag.generateTab(tab, e1);
	}
}
