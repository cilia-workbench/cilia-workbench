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
package fr.liglab.adele.cilia.workbench.designer.parser.chain.abstractcomposition;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.identifiable.NameNamespaceID;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.xml.XMLHelpers;
import fr.liglab.adele.cilia.workbench.designer.parser.element.common.IGenericMediator;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.MediatorSpec;
import fr.liglab.adele.cilia.workbench.designer.parser.element.spec.NameProperty;
import fr.liglab.adele.cilia.workbench.designer.service.common.Changeset;
import fr.liglab.adele.cilia.workbench.designer.service.common.MergeUtil;
import fr.liglab.adele.cilia.workbench.designer.service.specreposervice.SpecRepoService;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorSpecRef extends MediatorRef {

	public static final String XML_NODE_NAME = "mediator-specification";
	public static final String XML_SELECTION_CONSTRAINT = "selection-constraint";

	private List<PropertyConstraint> constraints = new ArrayList<PropertyConstraint>();

	public MediatorSpecRef(Node node, NameNamespaceID chainId) throws CiliaException {
		super(node, chainId);
		Node rootConstraint = XMLHelpers.findChild(node, XML_SELECTION_CONSTRAINT);
		if (rootConstraint != null) {
			Node[] sub = XMLHelpers.findChildren(rootConstraint, PropertyConstraint.XML_PROPERTY_CONSTRAINT);
			for (Node n : sub) {
				constraints.add(new PropertyConstraint(n));
			}
		}
	}

	public List<PropertyConstraint> getConstraints() {
		return constraints;
	}

	public List<NameProperty> getPossibleConstraints() {

		IGenericMediator ro = getReferencedObject();
		if (ro != null && ro instanceof MediatorSpec) {
			MediatorSpec spec = (MediatorSpec) ro;
			return spec.getProperties();
		}

		return new ArrayList<NameProperty>();
	}

	@Override
	public IGenericMediator getReferencedObject() {
		NameNamespaceID id = getReferencedTypeID();
		return SpecRepoService.getInstance().getMediatorForChain(id);
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		List<Changeset> retval = super.merge(other);
		MediatorSpecRef newInstance = (MediatorSpecRef) other;
		retval.addAll(MergeUtil.mergeLists(newInstance.getConstraints(), constraints));
		return retval;
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag[] tab = super.getErrorsAndWarnings();
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();
		for (CiliaFlag cf : tab)
			retval.add(cf);

		if (getReferencedObject() != null) {
			IGenericMediator ro = getReferencedObject();

			for (PropertyConstraint pc : constraints)
				if (ro.getProperty(pc.getName()) == null)
					retval.add(new CiliaError("Specification " + ro.getId().getName() + " doesn't reference a \""
							+ pc.getName() + "\" property", this));
		}

		return retval.toArray(new CiliaFlag[0]);
	}
}
