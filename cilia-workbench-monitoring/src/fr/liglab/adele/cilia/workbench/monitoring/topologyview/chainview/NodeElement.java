package fr.liglab.adele.cilia.workbench.monitoring.topologyview.chainview;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.workbench.monitoring.topologyview.providers.CiliaLabelProvider;

public class NodeElement extends LabelProvider {

	final protected MediatorReadOnly element;
	final protected CiliaLabelProvider labelProvider = new CiliaLabelProvider();
	
	public NodeElement(MediatorReadOnly element) {
		this.element = element;
	}
	
	public MediatorReadOnly getElement() {
		return element;
	}
	
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
