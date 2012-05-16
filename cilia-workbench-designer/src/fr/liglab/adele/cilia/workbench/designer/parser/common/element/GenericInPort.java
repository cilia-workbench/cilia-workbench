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
package fr.liglab.adele.cilia.workbench.designer.parser.common.element;

/**
 * Represents an in port. It can be a spec or an implementation.
 * 
 * @author Etienne Gandrille
 */
public class GenericInPort extends GenericPort {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericPort
	 * #isInPort()
	 */
	@Override
	public boolean isInPort() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.parser.common.element.GenericPort
	 * #isOutPort()
	 */
	@Override
	public boolean isOutPort() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.common.identifiable.Identifiable#getId()
	 */
	@Override
	public Object getId() {
		return "in:" + getName();
	}
}
