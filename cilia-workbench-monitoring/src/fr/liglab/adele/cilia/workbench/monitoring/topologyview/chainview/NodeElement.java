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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.CiliaLabelProvider;

/**
 * The Class NodeElement.
 * Used to provide the equals and hashcode methods to MediatorReadOnly.
 */
public class NodeElement extends LabelProvider {

	/** The element. */
	final protected MediatorReadOnly element;
	
	/** The label provider. */
	final protected CiliaLabelProvider labelProvider = new CiliaLabelProvider();
	
	/**
	 * Instantiates a new node element.
	 *
	 * @param element the embedded mediator.
	 */
	public NodeElement(MediatorReadOnly element) {
		this.element = element;
	}
	
	/**
	 * Gets the embedded mediator.
	 *
	 * @return the element
	 */
	public MediatorReadOnly getElement() {
		return element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if ( ! (obj instanceof NodeElement))
			return false;
		
		final NodeElement node = (NodeElement) obj;
		if (node.getElement().getId().equals(element.getId()))
			return true;
		else
			return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return element.getId().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object obj) {
		if (obj instanceof NodeElement) {
			NodeElement ne = (NodeElement) obj; 
			return labelProvider.getText(ne.getElement());
		}
		else
			return labelProvider.getText(obj);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object obj) {
		if (obj instanceof NodeElement) {
			NodeElement ne = (NodeElement) obj; 
			return labelProvider.getImage(ne.getElement());
		}
		else
			return labelProvider.getImage(obj);
	}
}
