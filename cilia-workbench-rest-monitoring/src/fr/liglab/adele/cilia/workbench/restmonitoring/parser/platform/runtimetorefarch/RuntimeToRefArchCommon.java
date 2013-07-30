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

import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.parser.element.ComponentDefinition;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.InAdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.InOutAdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.OutAdapterImplem;
import fr.liglab.adele.cilia.workbench.designer.parser.element.implem.MediatorImplem.RefMediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;

/**
 * 
 * @author Etienne Gandrille
 */
public class RuntimeToRefArchCommon {

	static String checkCompatible(ComponentDefinition reference, ComponentDefinition component) {

		// null values checking
		if (reference == null)
			return "reference is null";
		if (component == null)
			return "component is null";

		// Both are MediatorImplem
		if (reference instanceof MediatorImplem && component instanceof MediatorImplem) {
			if (reference != component) { // strong equality
				return "Mediator is of type " + component.getName() + " but should be " + reference.getName();
			}
			return null;
		}

		// MediatorSpec and MediatorImplem
		if (reference instanceof MediatorSpec && component instanceof MediatorImplem) {
			RefMediatorSpec runningMediatorSpec = ((MediatorImplem) component).getSpec();
			NameNamespaceID expectedSpecId = ((MediatorSpec) reference).getId();

			if (runningMediatorSpec == null)
				return "Mediator should implement " + expectedSpecId;

			NameNamespaceID curSpecId = runningMediatorSpec.getId();
			if (curSpecId.equals(expectedSpecId))
				return null;
			return "Mediator should implement " + expectedSpecId + " instead of " + curSpecId;
		}

		// Both are InAdapterImplem
		if (reference instanceof InAdapterImplem && component instanceof InAdapterImplem) {
			if (reference != component) { // strong equality
				return "InAdapter is of type " + component.getName() + " but should be " + reference.getName();
			}
			return null;
		}

		// Both are OutAdapterImplem
		if (reference instanceof OutAdapterImplem && component instanceof OutAdapterImplem) {
			if (reference != component) { // strong equality
				return "OutAdapter is of type " + component.getName() + " but should be " + reference.getName();
			}
			return null;
		}

		// Both are InOutAdapterImplem
		if (reference instanceof InOutAdapterImplem && component instanceof InOutAdapterImplem) {
			if (reference != component) { // strong equality
				return "InOutAdapter is of type " + component.getName() + " but should be " + reference.getName();
			}
			return null;
		}

		// Unknown types
		else {
			return component.getName() + " is not compatible with " + reference.getName();
		}
	}
}
