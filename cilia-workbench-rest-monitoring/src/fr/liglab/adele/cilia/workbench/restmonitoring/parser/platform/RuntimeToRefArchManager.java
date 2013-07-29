/**
 * Copyright 2012-2013 France Télécom 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.liglab.adele.cilia.workbench.common.misc.Strings;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class RuntimeToRefArchManager {

	public static Map<String, String> computeComponentPlatformIdToRefId(AbstractChain refChain, PlatformChain pfChain) {
		Map<String, String> retval = new HashMap<String, String>();
		if (refChain != null && pfChain != null) {
			// step 1 - using types
			retval = findComponentsLinksUsingTypes(refChain, pfChain);
			// step 2 - using bindings
			findComponentsLinksUsingBindings(refChain, pfChain, retval);
			// step 3 - using types and id
			findComponentsLinksUsingTypesAndIds(refChain, pfChain, retval);
			// step4 - using bindings (again!)
			findComponentsLinksUsingBindings(refChain, pfChain, retval);
			// step 5 - using id
			findComponentsLinksUsingIds(refChain, pfChain, retval);
			// step 6 - using bindings (again!)
			findComponentsLinksUsingBindings(refChain, pfChain, retval);
		}
		return retval;
	}

	/**
	 * Using compatibility between types in the reference architecture and in
	 * the platform chain, computes part of {@link #componentPlatformIdToRefId}
	 * map. The key idea in this algorithm is: if for a component in the
	 * platform chain, there is one and only one acceptable component in the
	 * reference architecture, here is a matching to be recorded into
	 * {@link #componentPlatformIdToRefId} map.
	 * 
	 * @param refChain
	 * @param rtChain
	 * @return
	 */
	private static Map<String, String> findComponentsLinksUsingTypes(AbstractChain refChain, PlatformChain rtChain) {
		Map<String, String> retval = new HashMap<String, String>();
		for (ComponentRef rtComponent : rtChain.getComponents()) {
			String rtId = rtComponent.getId();
			ComponentDefinition rtDef = rtComponent.getReferencedComponentDefinition();
			if (!Strings.isNullOrEmpty(rtId) && rtDef != null) {
				List<String> ids = getComponentsIdInAbstractChain(refChain, rtDef);
				if (ids.size() == 1) {
					retval.put(rtId, ids.get(0));
				}
			}
		}
		return retval;
	}

	/**
	 * Returns the list of components id which are part of the chain and which
	 * are compatible with the runtime component definiation.
	 * 
	 * @param chain
	 * @param def
	 * @return
	 */
	private static List<String> getComponentsIdInAbstractChain(AbstractChain chain, ComponentDefinition def) {
		List<String> retval = new ArrayList<String>();
		for (ComponentRef component : chain.getComponents()) {
			if (RuntimeToRefArchChecker.checkCompatible(component.getReferencedComponentDefinition(), def) == null)
				if (!Strings.isNullOrEmpty(component.getId()))
					retval.add(component.getId());
		}
		return retval;
	}

	private static int findComponentsLinksUsingBindings(AbstractChain refChain, PlatformChain rtChain, Map<String, String> componentPlatformIdToRefId) {
		int retval = 0;
		while (true) {
			int out = findComponentsLinksUsingOutgoingBindings(refChain, rtChain, componentPlatformIdToRefId);
			int in = findComponentsLinksUsingIncommingBindings(refChain, rtChain, componentPlatformIdToRefId);
			retval = retval + out + in;
			if (out + in == 0)
				return retval;
		}
	}

	private static int findComponentsLinksUsingOutgoingBindings(AbstractChain refChain, PlatformChain rtChain, Map<String, String> componentPlatformIdToRefId) {
		int retval = 0;
		if (refChain != null && rtChain != null && componentPlatformIdToRefId != null) {
			for (String rtId : componentPlatformIdToRefId.keySet()) {
				if (rtChain.getComponent(rtId) != null) {
					for (Binding rtBinding : rtChain.getComponent(rtId).getOutgoingBindings()) {
						ComponentRef rtRefDst = rtBinding.getDestinationComponentRef();
						String rtIdDst = rtBinding.getDestinationId();
						if (rtRefDst != null && !Strings.isNullOrEmpty(rtIdDst) && componentPlatformIdToRefId.get(rtIdDst) == null) {
							ComponentDefinition rtCompoDefDst = rtRefDst.getReferencedComponentDefinition();
							if (rtCompoDefDst != null) {
								String refId = componentPlatformIdToRefId.get(rtId);
								ComponentRef refCompoRef = refChain.getComponent(refId);
								if (refCompoRef != null) {
									List<String> matches = new ArrayList<String>();
									for (Binding refBinding : refCompoRef.getOutgoingBindings()) {
										String refIdDst = refBinding.getDestinationId();
										ComponentDefinition refCompoDefDst = refBinding.getDestinationComponentDefinition();
										if (!Strings.isNullOrEmpty(refIdDst) && RuntimeToRefArchChecker.checkCompatible(refCompoDefDst, rtCompoDefDst) == null)
											matches.add(refIdDst);
									}
									// Match found !
									if (matches.size() == 1) {
										componentPlatformIdToRefId.put(rtIdDst, matches.get(0));
										retval++;
									}
								}
							}
						}
					}
				}
			}
		}
		return retval;
	}

	private static int findComponentsLinksUsingIncommingBindings(AbstractChain refChain, PlatformChain rtChain, Map<String, String> componentPlatformIdToRefId) {
		int retval = 0;
		if (refChain != null && rtChain != null && componentPlatformIdToRefId != null) {
			for (String rtId : componentPlatformIdToRefId.keySet()) {
				if (rtChain.getComponent(rtId) != null) {
					for (Binding rtBinding : rtChain.getComponent(rtId).getIncommingBindings()) {
						ComponentRef rtRefSrc = rtBinding.getSourceComponentRef();
						String rtIdSrc = rtBinding.getSourceId();
						if (rtRefSrc != null && !Strings.isNullOrEmpty(rtIdSrc) && componentPlatformIdToRefId.get(rtIdSrc) == null) {
							ComponentDefinition rtCompoDefSrc = rtRefSrc.getReferencedComponentDefinition();
							if (rtCompoDefSrc != null) {
								String refId = componentPlatformIdToRefId.get(rtId);
								ComponentRef refCompoRef = refChain.getComponent(refId);
								if (refCompoRef != null) {
									List<String> matches = new ArrayList<String>();
									for (Binding refBinding : refCompoRef.getIncommingBindings()) {
										String refIdSrc = refBinding.getSourceId();
										ComponentDefinition refCompoDefSrc = refBinding.getSourceComponentDefinition();
										if (!Strings.isNullOrEmpty(refIdSrc) && RuntimeToRefArchChecker.checkCompatible(refCompoDefSrc, rtCompoDefSrc) == null)
											matches.add(refIdSrc);
									}
									// Match found !
									if (matches.size() == 1) {
										componentPlatformIdToRefId.put(rtIdSrc, matches.get(0));
										retval++;
									}
								}
							}
						}
					}
				}
			}
		}
		return retval;
	}

	private static int findComponentsLinksUsingTypesAndIds(AbstractChain refChain, PlatformChain rtChain, Map<String, String> componentPlatformIdToRefId) {
		int retval = 0;
		for (ComponentRef rtCompo : rtChain.getComponents()) {
			String rtId = rtCompo.getId();
			ComponentDefinition rtCompoDef = rtCompo.getReferencedComponentDefinition();
			if (componentPlatformIdToRefId.get(rtId) == null && rtCompoDef != null && !Strings.isNullOrEmpty(rtId)) {
				String result = null;
				List<String> ids = getComponentsIdInAbstractChain(refChain, rtCompoDef);
				for (Iterator<String> iterator = ids.iterator(); iterator.hasNext() && result == null;) {
					String id = iterator.next();
					if (isLinkBetweenId(id, rtId))
						result = id;
				}
				if (result != null) {
					componentPlatformIdToRefId.put(rtId, result);
					retval++;
				}
			}
		}
		return retval;
	}

	private static int findComponentsLinksUsingIds(AbstractChain refChain, PlatformChain rtChain, Map<String, String> componentPlatformIdToRefId) {
		int retval = 0;
		for (ComponentRef rtCompo : rtChain.getComponents()) {
			String rtId = rtCompo.getId();
			if (componentPlatformIdToRefId.get(rtId) == null && !Strings.isNullOrEmpty(rtId)) {
				String result = null;
				ComponentRef[] components = refChain.getComponents();
				for (int i = 0; i < components.length && result == null; i++) {
					String id = components[i].getId();
					if (isLinkBetweenId(id, rtId))
						result = id;
				}
				if (result != null) {
					componentPlatformIdToRefId.put(rtId, result);
					retval++;
				}
			}
		}
		return retval;
	}

	private static boolean isLinkBetweenId(String refArchiId, String runningId) {
		if (refArchiId == null || runningId == null)
			return false;
		return runningId.startsWith(refArchiId);
	}
}
