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
package fr.liglab.adele.cilia.workbench.designer.parser.spec;

import java.util.Iterator;
import java.util.List;

import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.propertyview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class PullElementUtil implements DisplayedInPropertiesView {

	public static SpecFile pullRepoElement(List<SpecFile> elements, String id) {
		for (Iterator<SpecFile> itr = elements.iterator(); itr.hasNext();) {
			SpecFile element = itr.next();
			if (element.getFilePath().equals(id)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static MediatorSpec pullMediatorSpec(SpecModel elements, String id, String namespace) {
		for (Iterator<MediatorSpec> itr = elements.getMediatorSpecs().iterator(); itr.hasNext();) {
			MediatorSpec element = itr.next();
			if (element.getId().equals(id) && element.getNamespace().equals(namespace)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Port pullPort(MediatorSpec mediator, String name, Class<? extends Port> classType) {
		for (Iterator<Port> itr = mediator.getPorts().iterator(); itr.hasNext();) {
			Port element = itr.next();
			if (element.getName().equals(name) && element.getClass().equals(classType)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Property pullProperty(MediatorSpec mediator, String key) {
		for (Iterator<Property> itr = mediator.getProperties().iterator(); itr.hasNext();) {
			Property element = itr.next();
			if (element.getKey().equals(key)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}

	public static Parameter pullParameter(ComponentPart componentPart, String name) {
		for (Iterator<Parameter> itr = componentPart.getParameters().iterator(); itr.hasNext();) {
			Parameter element = itr.next();
			if (element.getName().equals(name)) {
				itr.remove();
				return element;
			}
		}
		return null;
	}
}
