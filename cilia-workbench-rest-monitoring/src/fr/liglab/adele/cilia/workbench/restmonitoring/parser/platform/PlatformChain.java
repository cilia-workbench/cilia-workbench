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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.marker.IdentifiableUtils;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.MergeUtil;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class PlatformChain implements DisplayedInPropertiesView, ErrorsAndWarningsFinder, Identifiable, Mergeable {

	private final String name;
	private final PlatformModel platform;
	private List<RunningMediator> mediators = new ArrayList<RunningMediator>();
	private List<RunningAdapter> adapters = new ArrayList<RunningAdapter>();
	private List<RunningBinding> bindings = new ArrayList<RunningBinding>();

	public PlatformChain(String name, PlatformModel platform) {
		this.name = name;
		this.platform = platform;
	}

	public PlatformChain(JSONObject json, PlatformModel platform) throws CiliaException {
		this.platform = platform;

		try {
			name = json.getString("Chain");
		} catch (JSONException e) {
			throw new CiliaException("error while parsing name", e);
		}

		try {
			JSONArray mediatorsList = json.getJSONArray("Mediators");
			for (int i = 0; i < mediatorsList.length(); i++) {
				String mediatorName = (String) mediatorsList.get(i);
				mediators.add(new RunningMediator(mediatorName));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing mediators list", e);
		}

		try {
			JSONArray adaptersList = json.getJSONArray("Adapters");
			for (int i = 0; i < adaptersList.length(); i++) {
				String adapterName = (String) adaptersList.get(i);
				adapters.add(new RunningAdapter(adapterName));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}

		try {
			JSONArray bindingsList = json.getJSONArray("Bindings");
			for (int i = 0; i < bindingsList.length(); i++) {
				JSONObject binding = (JSONObject) bindingsList.get(i);
				bindings.add(new RunningBinding(binding));
			}
		} catch (JSONException e) {
			throw new CiliaException("error while parsing adapters list", e);
		}
	}

	public List<RunningMediator> getMediators() {
		return mediators;
	}

	public List<RunningAdapter> getAdapters() {
		return adapters;
	}

	public Object getComponent(String name) {
		for (RunningMediator mediator : mediators)
			if (mediator.getName().equalsIgnoreCase(name))
				return mediator;
		for (RunningAdapter adapter : adapters)
			if (adapter.getName().equalsIgnoreCase(name))
				return adapter;
		return null;
	}

	public List<RunningBinding> getBindings() {
		return bindings;
	}

	public List<RunningBinding> getIncomingBindings(String element) {
		List<RunningBinding> retval = new ArrayList<RunningBinding>();

		for (RunningBinding binding : bindings) {
			if (binding.getDestinationId().equals(element))
				retval.add(binding);
		}

		return retval;
	}

	public List<RunningBinding> getOutgoingBindings(String element) {
		List<RunningBinding> retval = new ArrayList<RunningBinding>();

		for (RunningBinding binding : bindings) {
			if (binding.getSourceId().equals(element))
				retval.add(binding);
		}

		return retval;
	}

	@Override
	public Object getId() {
		return name;
	}

	public String getName() {
		return name;
	}

	public PlatformModel getPlatform() {
		return platform;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = new ArrayList<Changeset>();
		PlatformChain newInstance = (PlatformChain) other;

		retval.addAll(MergeUtil.mergeLists(newInstance.getAdapters(), adapters));
		retval.addAll(MergeUtil.mergeLists(newInstance.getMediators(), mediators));
		retval.addAll(MergeUtil.mergeLists(newInstance.getBindings(), bindings));

		for (Changeset c : retval)
			c.pushPathElement(this);

		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		List<CiliaFlag> list = new ArrayList<CiliaFlag>();

		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");

		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, adapters));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, mediators));
		list.addAll(IdentifiableUtils.getErrorsNonUniqueId(this, bindings));

		return CiliaFlag.generateTab(list, e1);
	}
}
