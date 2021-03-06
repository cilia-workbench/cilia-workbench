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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.runtimetorefarch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Binding;
import fr.liglab.adele.cilia.workbench.common.parser.chain.Cardinality;
import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition.AbstractBinding;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;

/**
 * 
 * @author Etienne Gandrille
 */
public class RuntimeToRefArchChecker {

	public static List<CiliaFlag> checkComponent(ComponentRef rtComponent) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		// no reference architecture or not found
		PlatformChain chain = (PlatformChain) rtComponent.getChain();
		if (chain.getRefArchitecture() == null)
			return retval;

		// finds component in reference architecture
		ComponentRef refComponent = null;
		try {
			refComponent = chain.getComponentInReferenceArchitecture(rtComponent);
		} catch (CiliaException e) {
			String msg = e.getMessage();
			retval.add(new CiliaError(msg, rtComponent));
			return retval;
		}

		// types checking
		ComponentDefinition refDefinition = refComponent.getReferencedComponentDefinition();
		ComponentDefinition rtDefinition = rtComponent.getReferencedComponentDefinition();
		// null values are checked out of this method
		if (refDefinition != null && rtDefinition != null) {
			String msg = RuntimeToRefArchCommon.checkCompatible(refDefinition, rtDefinition);
			if (msg != null) {
				retval.add(new CiliaError(msg, rtComponent));
				return retval;
			}
		}

		// bindings checking
		for (CiliaFlag error : RuntimeToRefArchChecker.checkBindings(refComponent, rtComponent))
			retval.add(error);

		return retval;
	}

	private static List<CiliaFlag> checkBindings(ComponentRef reference, ComponentRef current) {
		List<CiliaFlag> retval = checkOutgoingBindings(reference, current);
		retval.addAll(checkIncommingBindings(reference, current));
		return retval;
	}

	private static List<CiliaFlag> checkOutgoingBindings(ComponentRef reference, ComponentRef current) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		PlatformChain curChain = (PlatformChain) current.getChain();

		Binding[] refBindings = reference.getOutgoingBindings();
		Binding[] curBindings = current.getOutgoingBindings();

		// map init
		Map<Binding, Integer> bindingCpt = new HashMap<Binding, Integer>();
		for (Binding b : refBindings) {
			bindingCpt.put(b, 0);
		}

		for (Binding curBinding : curBindings) {
			boolean found = false;
			for (Binding refBinding : refBindings) {

				// cur
				String curDstId = curBinding.getDestinationId();
				String curDstPort = curBinding.getDestinationPort();

				// ref
				String refDstId = refBinding.getDestinationId();
				String refDstPort = refBinding.getDestinationPort();

				// check if no information is missing
				if (curDstId != null && curDstPort != null && refDstId != null && refDstPort != null) {
					// ports must be the same
					if (curDstPort.equals(refDstPort)) {
						try {
							String id = curChain.getIdInReferenceArchitecture(curDstId);
							if (id.equals(refDstId)) {
								found = true;
								bindingCpt.put(refBinding, bindingCpt.get(refBinding) + 1);
							}
						} catch (CiliaException e) {
							// no match
						}
					}
				}
			}

			if (found == false) {
				retval.add(new CiliaError("The binding " + curBinding + " is not allowed", curBinding));
			}
		}

		// cardinality check
		for (Binding b : refBindings) {
			int nb = bindingCpt.get(b);
			Cardinality card = ((AbstractBinding) b).getDestinationCardinality();
			if (card.getMinValue() > nb)
				retval.add(new CiliaError("The outgoing binding " + b + " is instanciated " + nb + " time(s) instead of a minimum of " + card.getMinValue()
						+ " time(s)", curChain));
			if (!card.isInfiniteBoundary() && card.getMaxValue() < nb)
				retval.add(new CiliaError("The outgoing binding " + b + " is instanciated " + nb + " time(s) instead of a maximum of " + card.getMaxValue()
						+ " time(s)", curChain));
		}

		return retval;
	}

	private static List<CiliaFlag> checkIncommingBindings(ComponentRef reference, ComponentRef current) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		PlatformChain curChain = (PlatformChain) current.getChain();

		Binding[] refBindings = reference.getIncommingBindings();
		Binding[] curBindings = current.getIncommingBindings();

		// map init
		Map<Binding, Integer> bindingCpt = new HashMap<Binding, Integer>();
		for (Binding b : refBindings) {
			bindingCpt.put(b, 0);
		}

		for (Binding curBinding : curBindings) {
			boolean found = false;
			for (Binding refBinding : refBindings) {

				// cur
				String curSrcId = curBinding.getSourceId();
				String curSrcPort = curBinding.getSourcePort();

				// ref
				String refSrcId = refBinding.getSourceId();
				String refSrcPort = refBinding.getSourcePort();

				// check if no information is missing
				if (curSrcId != null && curSrcPort != null && refSrcId != null && refSrcPort != null) {
					// ports must be the same
					if (curSrcPort.equals(refSrcPort)) {
						try {

							String id = curChain.getIdInReferenceArchitecture(curSrcId);
							if (id.equals(refSrcId)) {
								found = true;
								bindingCpt.put(refBinding, bindingCpt.get(refBinding) + 1);
							}
						} catch (CiliaException e) {
							// no match
						}
					}
				}
			}

			if (found == false) {
				retval.add(new CiliaError("The binding " + curBinding + " is not allowed", curBinding));
			}
		}

		// cardinality check
		for (Binding b : refBindings) {
			int nb = bindingCpt.get(b);
			Cardinality card = ((AbstractBinding) b).getSourceCardinality();
			if (card.getMinValue() > nb)
				retval.add(new CiliaError("The incomming binding " + b + " is instanciated " + nb + " time(s) instead of a minimum of " + card.getMinValue()
						+ " time(s)", curChain));
			if (!card.isInfiniteBoundary() && card.getMaxValue() < nb)
				retval.add(new CiliaError("The incomming binding " + b + " is instanciated " + nb + " time(s) instead of a maximum of " + card.getMaxValue()
						+ " time(s)", curChain));
		}

		return retval;
	}
}